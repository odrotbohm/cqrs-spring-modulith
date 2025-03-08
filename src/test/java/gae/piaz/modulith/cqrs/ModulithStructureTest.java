package gae.piaz.modulith.cqrs;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModulithStructureTest {

    ApplicationModules modules = ApplicationModules.of(CQRSApplication.class);

    @Test
    void verifiesModularStructure() {
        modules.verify();
    }

    @Test
    void createModuleDocumentation() {
        new Documenter(modules, "generated-docs/uml").writeDocumentation().writeModulesAsPlantUml();
    }

    @Test
    void writeDocumentationSnippets() {
        new Documenter(modules, "generated-docs/canvases").writeModuleCanvases();
    }
} 