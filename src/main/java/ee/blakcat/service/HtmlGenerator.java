package ee.blakcat.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ee.blakcat.model.MetadataHolder;
import ee.blakcat.util.ManagerFileUtils;

public class HtmlGenerator {
    
    public String generate(Set<MetadataHolder> metadataHolders) {
        var template = ManagerFileUtils.getTemplate();
        List<String> result = new LinkedList<>();

        for (String string : template) {
            if (string.contains("let items = []")) {
                result.add("let items = [");
                appendItems(metadataHolders, result);
                result.add("]");
            } else {
                result.add(string);
            }
        }
        return String.join("\n", result);
    }

    private void appendItems(Set<MetadataHolder> metadataHolders, List<String> result) {
        for (MetadataHolder metadataHolder : metadataHolders) {
            result.add("{");
            result.add("prompt: \"" + sanitize(metadataHolder.getPrompt()) + "\",");
            result.add("negative: \"" + sanitize(metadataHolder.getNegative()) + "\",");
            result.add("steps: " + metadataHolder.getSteps() + ",");
            result.add("sampler: \"" + metadataHolder.getSampler() + "\",");
            result.add("scale: \"" + metadataHolder.getScale() + "\",");
            result.add("size: \"" + metadataHolder.getSize() + "\",");
            result.add("model: \"" + metadataHolder.getModel() + "\",");
            result.add("seed: \"" + metadataHolder.getSeed() + "\",");
            result.add("path: \"" + sanitize(metadataHolder.getPath()) + "\",");
            result.add("},");
        }
    }
    
    private String sanitize(String string) {
        if (string == null || string.isBlank()) {
            return "";
        }
        return string.replace("\\", "/").replace("<", "&#60;").replace(">", "&#62;");
    }

}
