package br.com.bank.api_releases.forms;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class DeleteListForm {

    @NotNull(message = "Este campo não pode estar vazio!")
    @NotEmpty(message = "Este campo não pode estar vazio!")
    List<UUID> ids;
}
