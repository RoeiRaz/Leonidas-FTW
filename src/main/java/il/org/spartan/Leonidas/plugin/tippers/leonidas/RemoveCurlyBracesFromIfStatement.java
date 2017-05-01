package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import static il.org.spartan.Leonidas.plugin.leonidas.GenericPsiElementStub.booleanExpression;
import static il.org.spartan.Leonidas.plugin.leonidas.GenericPsiElementStub.statement;

/**
 * Remove redundant curly braces
 * RemoveCurlyBraces
 * author Oren Afek, Shron Kuninin, michalcohen
 * since 06/01/17
 */
public class RemoveCurlyBracesFromIfStatement implements LeonidasTipperDefinition {
    @Override
    public void constraints() {
    }

    @Override
    public void matcher() {
        new Template(() -> {
            if (booleanExpression(0)) {
                statement(1);
            }
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            if (booleanExpression(0))
                statement(1);
        });
    }
}
