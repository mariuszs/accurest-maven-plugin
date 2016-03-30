package io.codearte.accurest.maven;

import static java.lang.String.format;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import io.codearte.accurest.AccurestException;
import io.codearte.accurest.TestGenerator;
import io.codearte.accurest.config.AccurestConfigProperties;

@Mojo(name = "generateSpecs", defaultPhase = LifecyclePhase.GENERATE_TEST_SOURCES)
public class GenerateSpecsMojo extends AbstractMojo {

	@Parameter(defaultValue = "${basedir}", readonly = true)
	private File baseDir;

	@Parameter(defaultValue = "${project.build.directory}", readonly = true)
	private File projectBuildDirectory;

	@Parameter(property = "contractsDir", defaultValue = "/src/test/resources/contracts")
	private String contractsDir;

	@Parameter(property = "generatedTestSourcesDir", defaultValue = "/generated-test-sources/accurest")
	private String generatedTestSourcesDir;

	@Parameter(property = "basePackageForTests", defaultValue = "io.codearte.accurest.tests")
	private String basePackageForTests;

	@Parameter(property = "baseClassForTests")
	private String baseClassForTests;

	public GenerateSpecsMojo() {
	}

	public void execute() throws MojoExecutionException, MojoFailureException {
		AccurestConfigProperties config = new AccurestConfigProperties();

		config.setContractsDslDir(new File(baseDir, contractsDir));
		config.setBasePackageForTests(basePackageForTests);
		config.setGeneratedTestSourcesDir(new File(projectBuildDirectory, generatedTestSourcesDir));
		config.setBaseClassForTests(baseClassForTests);

		getLog().info("Accurest Plugin: Invoking test sources generation");
		getLog().info(format("Using %s as test source directory", config.getGeneratedTestSourcesDir()));
		getLog().info(format("Using %s as base class for tests", config.getBaseClassForTests()));

		try {
			TestGenerator generator = new TestGenerator(config);
			int generatedClasses = generator.generate();
			getLog().info(format("Generated %s test classes.", generatedClasses));
		} catch (AccurestException e) {
			throw new MojoExecutionException(format("Accurest Plugin exception: %s", e.getMessage()), e);
		}

	}

}