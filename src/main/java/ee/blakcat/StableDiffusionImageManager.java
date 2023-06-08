package ee.blakcat;

import java.util.List;

import ee.blakcat.model.DeduplicateMode;
import ee.blakcat.service.ImageManager;

public class StableDiffusionImageManager {
    public static void main(String[] args) {
        List<String> argList = List.of(args);
        var mode = DeduplicateMode.from(argList);
        var folder = calculateFolder(argList);
        new ImageManager(folder, mode, argList.contains("-deforum")).start();
    }

    private static String calculateFolder(List<String> argList) {
        if (argList.contains("-path")) {
            return argList.get(argList.indexOf("-path") + 1);
        } else {
            return "";
        }

    }
}