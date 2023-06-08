package ee.blakcat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetadataHolder {
    
    private String prompt;
    private String negative;
    private int steps;
    private String sampler;
    private String scale;
    private String size;
    private String model;
    @EqualsAndHashCode.Exclude
    private String seed;
    @EqualsAndHashCode.Exclude
    private String path;
    
}
