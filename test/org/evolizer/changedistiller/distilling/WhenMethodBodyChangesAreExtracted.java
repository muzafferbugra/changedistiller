package org.evolizer.changedistiller.distilling;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.evolizer.changedistiller.model.classifiers.java.JavaEntityType;
import org.evolizer.changedistiller.model.entities.StructureEntityVersion;
import org.evolizer.changedistiller.treedifferencing.Node;
import org.evolizer.changedistiller.util.Compilation;
import org.evolizer.changedistiller.util.CompilationUtils;
import org.evolizer.changedistiller.util.TreeTransformerUtils;
import org.junit.Test;

// simple test cases. exhaustive test cases with classification check in separate tests suite
public class WhenMethodBodyChangesAreExtracted extends WhenChangesAreExtracted {

    private final static String TEST_DATA = "src_change/";

    @Test
    public void unchangedMethodBodyShouldNotHaveAnyChanges() throws Exception {
        Compilation compilation = CompilationUtils.compileFile(TEST_DATA + "TestLeft.java");
        Node rootLeft = TreeTransformerUtils.convertMethodBody("foo", compilation);
        Node rootRight = TreeTransformerUtils.convertMethodBody("foo", compilation);
        StructureEntityVersion structureEntity = new StructureEntityVersion(JavaEntityType.METHOD, "foo", 0);
        Distiller distiller = getDistiller(structureEntity);
        distiller.extractClassifiedSourceCodeChanges(rootLeft, rootRight);
        assertThat(structureEntity.getSourceCodeChanges().isEmpty(), is(true));
    }

    @Test
    public void changedMethodBodyShouldHaveChanges() throws Exception {
        Compilation compilationLeft = CompilationUtils.compileFile(TEST_DATA + "TestLeft.java");
        Compilation compilationRight = CompilationUtils.compileFile(TEST_DATA + "TestRight.java");
        Node rootLeft = TreeTransformerUtils.convertMethodBody("foo", compilationLeft);
        Node rootRight = TreeTransformerUtils.convertMethodBody("foo", compilationRight);
        StructureEntityVersion structureEntity = new StructureEntityVersion(JavaEntityType.METHOD, "foo", 0);
        Distiller distiller = getDistiller(structureEntity);
        distiller.extractClassifiedSourceCodeChanges(rootLeft, rootRight);
        assertThat(structureEntity.getSourceCodeChanges().size(), is(5));
    }

}