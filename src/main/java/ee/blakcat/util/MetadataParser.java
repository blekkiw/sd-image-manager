package ee.blakcat.util;

import ee.blakcat.model.MetadataHolder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MetadataParser {
    
    public static MetadataHolder buildMetadataHolderFromString(String string) {
        var negativeExists = string.contains("Negative prompt:");
        var negativeAnchor = negativeExists ? "Negative prompt:" : "Steps:";
        var prompt = string.substring(string.indexOf("parameters:"), string.indexOf(negativeAnchor)).replace("parameters: ", "").trim();
        var negative = negativeExists ? string.substring(string.indexOf("Negative prompt:"), string.indexOf("Steps:")).replace("Negative prompt: ", "").trim() : "";
        var steps = string.substring(string.indexOf("Steps:"), string.indexOf(", Sampler:")).replace("Steps: ", "");
        var sampler = string.substring(string.indexOf("Sampler:"), string.indexOf(", CFG scale:")).replace("Sampler: ", "");
        var scale = string.substring(string.indexOf("CFG scale:"), string.indexOf(", Seed:")).replace("CFG scale: ", "");
        var seed = string.substring(string.indexOf("Seed:"), string.indexOf(", Size:")).replace("Seed: ", "");
        var size = string.substring(string.indexOf("Size:"), string.indexOf(", Model hash:")).replace("Size: ", "");
        var model = string.substring(string.indexOf("Model hash:"), string.indexOf(", Model:")).replace("Model hash: ", "");
        return MetadataHolder.builder()
                .prompt(prompt)
                .negative(negative)
                .steps(Integer.parseInt(steps))
                .sampler(sampler)
                .scale(scale)
                .seed(seed)
                .size(size)
                .model(model)
                .build();
    }
}
