package docker.dockerinterlockconnection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageSearchDto {
    private String name;
    private String description;
    private Integer stars;
}
