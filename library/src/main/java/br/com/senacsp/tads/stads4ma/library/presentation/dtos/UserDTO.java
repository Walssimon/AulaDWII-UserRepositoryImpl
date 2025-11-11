package br.com.senacsp.tads.stads4ma.library.presentation.dtos;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class UserDTO {

    private UUID id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String name;

    @NotBlank(message = "Email é obrigatório")
    @Size(max = 30, message = "O email deve ter no máximo 30 caracteres")
    @Email(message = "O email deve ser válido")
    private String email;

    @NotBlank(message = "O password é obrigatório")
    @Size(min = 6, max = 12, message = "O Password deve ter entre 6 a 12 caracteres")
    private String password;

    public static UserDTO fromEntity(User user) {
        if (user == null) return null;
        return UserDTO.builder()
                .id(user.getID())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public static User fromDTO(UserDTO user) {
        if (user == null) return null;
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}

