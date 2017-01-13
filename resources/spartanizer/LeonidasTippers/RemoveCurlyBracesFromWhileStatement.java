package spartanizer.LeonidasTippers;

import com.intellij.psi.PsiIfStatement;
import com.intellij.psi.PsiWhileStatement;
import il.org.spartan.ispartanizer.plugin.leonidas.GenericPsiElementStub;
import il.org.spartan.ispartanizer.plugin.leonidas.Leonidas;

/**
 * @author Oren Afek
 * @since 01-12-17
 */
public class RemoveCurlyBracesFromWhileStatement extends GenericPsiElementStub {

    @Leonidas(value = PsiWhileStatement.class)
    public void from() {
        while (booleanExpression(0)) {
            statement(1);
        }
    }

    @Leonidas(value = PsiWhileStatement.class)
    public void to() {
        while (booleanExpression(0))
            statement(1);
    }
}
