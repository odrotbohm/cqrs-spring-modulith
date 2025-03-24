package gae.piaz.modulith.cqrs;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;
import org.springframework.modulith.docs.Documenter.Options;

class ModulithStructureTest {

	ApplicationModules modules = ApplicationModules.of(CQRSApplication.class);

	@Test
	void verifiesModularStructure() {
		modules.verify();
	}

	@Test
	void createModuleDocumentation() {

		var options = Options.defaults().withOutputFolder("generated-docs/uml");

		new Documenter(modules, options)
				.writeDocumentation()
				.writeModulesAsPlantUml();
	}

	@Test
	void writeDocumentationSnippets() {

		var options = Options.defaults().withOutputFolder("generated-docs/canvases");

		new Documenter(modules, options)
				.writeModuleCanvases();
	}
}
