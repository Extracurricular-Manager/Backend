package fr.periscol.backend.service.dto.service_model;

import javax.validation.constraints.NotNull;

public class NewServiceMetadataDTO {

    @NotNull(message = "Model cannot be null")
    private String model;
    @NotNull(message = "Name cannot be null")
    private String name;
    @NotNull(message = "Icon cannot be null")
    private String icon;

    public String getModel() {
        return model;
    }

    public void setModel(String module) {
        this.model = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}