package com.example.HuceBack.dto.Image;

import com.example.HuceBack.dto.common.Const;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageRequest {
    @NotNull
    private String type;
    @NotNull
    private MultipartFile file;
    private Long postId;
    private Long commentId;
}
