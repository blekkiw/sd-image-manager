package ee.blakcat.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import ee.blakcat.model.DeduplicateMode;
import ee.blakcat.model.MetadataHolder;
import ee.blakcat.util.ManagerFileUtils;
import ee.blakcat.util.MetadataParser;
import lombok.SneakyThrows;

public class ImageManager {

    
    private final String baseFolder;
    private final DeduplicateMode mode;
    private final boolean removeDeforum;

    public ImageManager(String baseFolder, DeduplicateMode mode, boolean removeDeforum) {
        this.baseFolder = baseFolder;
        this.mode = mode;
        this.removeDeforum = removeDeforum;
    }

    @SneakyThrows
    private void generateHtmlReport() {
        Set<MetadataHolder> files = ManagerFileUtils.getFilesFromFolder(baseFolder).stream()
                .filter(this::isFileSupported)
                .map(this::mapToHolder)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Files.write(Paths.get(baseFolder, "report.html"), new HtmlGenerator().generate(files).getBytes());
    }

    private void deduplicateCopy() {
        List<Path> files = ManagerFileUtils.getFilesFromFolder(baseFolder);
        var duplicates = findDuplicates(files);

        for (Path duplicate : duplicates) {
            processDuplicate(duplicate);
        }
        System.out.println("Total files: " + files.size());
        System.out.println("Duplicates copied: " + duplicates.size());
    }

    private void deduplicateRemove() {
        List<Path> files = ManagerFileUtils.getFilesFromFolder(baseFolder);
        var duplicates = findDuplicates(files);

        for (Path duplicate : duplicates) {
            ManagerFileUtils.safeDeleteFile(duplicate.toString());
        }
        System.out.println("Total files: " + files.size());
        System.out.println("Duplicates removed: " + duplicates.size());
    }


    private void rollback() {
        List<Path> files = ManagerFileUtils.getFilesFromFolder(baseFolder, f -> f.toString().contains(ManagerFileUtils.DUPLICATE));
        Set<String> duplicateFolders = files.stream().map(f -> f.getParent().toString()).collect(Collectors.toSet());
        for (Path file : files) {
            try {
                Files.move(file, Paths.get(file.getParent().getParent().toString(), file.getFileName().toString().replace(ManagerFileUtils.DUPLICATE, "")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (String duplicateFolder : duplicateFolders) {
            ManagerFileUtils.safeDeleteFile(duplicateFolder);
        }
        System.out.println("Duplicates rolled back: " + files.size());
        System.out.println("Folders deleted: " + duplicateFolders.size());
    }

    private void printInfo() {
        List<Path> files = ManagerFileUtils.getFilesFromFolder(baseFolder);
        System.out.println("Files found: " + files.size());
        System.out.println("Files total size: " + files.stream().mapToLong(f -> f.toFile().length()).sum() / 1024 / 1024 + " MB");
        var duplicates = findDuplicates(files);
        System.out.println("Duplicates found: " + duplicates.size());
        System.out.println("Duplicates total size: " + duplicates.stream().mapToLong(f -> f.toFile().length()).sum() / 1024 / 1024 + " MB");
    }

    @SneakyThrows
    private Set<Path> findDuplicates(List<Path> files) {
        var originals = files.stream()
                .parallel()
                .filter(this::isFileSupported)
                .map(this::mapToHolder)
                .filter(Objects::nonNull)
                .distinct()
                .map(holder -> Path.of(holder.getPath()))
                .collect(Collectors.toSet());
        return files.stream()
                .parallel()
                .filter(path -> (!originals.contains(path) && isFileSupported(path)) || isDeforumFile(path))
                .collect(Collectors.toSet());
    }

    private MetadataHolder mapToHolder(Path path) {
        try {
            var holder = buildMetadataHolder(Imaging.getMetadata(path.toFile()));
            if (holder == null) {
                return null;
            }
            holder.setPath(path.toString());
            return holder;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private boolean isDeforumFile(Path path) {
        return (removeDeforum && path.toString().contains("Deforum") && isImage(path));
    }

    private boolean isFileSupported(Path path) {
        return !path.toString().contains(ManagerFileUtils.DUPLICATE) && !path.toString().contains("Deforum") && isImage(path);
    }
    
    private boolean isImage(Path path) {
        return (path.toString().endsWith(".png") || path.toString().endsWith(".jpg") || path.toString().endsWith(".jpeg"));
    }

    @SneakyThrows
    private static void processDuplicate(Path duplicate) {
        ManagerFileUtils.safeCreateFolder(duplicate.getParent());
        Files.move(duplicate, Paths.get(duplicate.getParent().toString(), ManagerFileUtils.DUPLICATE, duplicate.getFileName().toString()));
    }

    private static MetadataHolder buildMetadataHolder(ImageMetadata metadata) {
        if (metadata == null) {
            return null;
        }
        for (ImageMetadata.ImageMetadataItem item : metadata.getItems()) {
            if (item.toString().contains("parameters")) {
                return MetadataParser.buildMetadataHolderFromString(item.toString());
            }
        }
        return null;
    }

    public void start() {
        switch (mode) {
            case COPY -> deduplicateCopy();
            case ROLLBACK -> rollback();
            case INFO -> printInfo();
            case REMOVE -> deduplicateRemove();
            case REPORT -> generateHtmlReport();
            default -> throw new IllegalArgumentException("Unknown mode: " + mode);
        }
    }
}
