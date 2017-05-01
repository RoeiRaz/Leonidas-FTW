package il.org.spartan.Leonidas.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.auxilary_layer.type;
import il.org.spartan.Leonidas.plugin.tippers.*;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;
import il.org.spartan.Leonidas.plugin.utils.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.*;

/**
 * @author Oren Afek, michalcohen
 * @since 01-12-2016
 */
public class Toolbox implements ApplicationComponent {

    private static final Logger logger = new Logger(Toolbox.class);
    private final Map<Class<? extends PsiElement>, List<Tipper>> tipperMap = new HashMap<>();
    private final Set<VirtualFile> excludedFiles = new HashSet<>();
    private final Set<Class<? extends PsiElement>> operableTypes = new HashSet<>();
    public boolean playground = false;

    public static Toolbox getInstance() {
        return (Toolbox) ApplicationManager.getApplication().getComponent(Toolbox.auxGetComponentName());
    }

    private static String auxGetComponentName() {
        return Toolbox.class.getSimpleName();
    }

    public List<Tipper> getAllTippers() {
        List<Tipper> list = new ArrayList<>();
        this.tipperMap.values().forEach(element -> {
            element.forEach(tipper -> {
                list.add(tipper);
            });
        });
        return list;
    }

    private void initializeInstance() {
        this
                .add(new SafeReference())
                .add(new Unless())
                .add(new LambdaExpressionRemoveRedundantCurlyBraces()) //
                .add(new LispLastElement())
                .add(new DefaultsTo())
                .add(new MethodDeclarationRenameSingleParameterToCent())//
                .add(new Delegator());
        createLeonidasTippers();
    }

    private void createLeonidasTippers() {
        (new Reflections(LeonidasTipperDefinition.class)).getSubTypesOf(LeonidasTipperDefinition.class).stream()
                .forEach(c -> {
                    try {
                        String source = Utils.getSourceCode(c);
                        if (!source.equals(""))
                            add(new LeonidasTipper(c.getSimpleName(), source));
                    } catch (IOException e) {
                        logger.info("Failed to read file: " + c.getName() + "\n" + Arrays.stream(e.getStackTrace()).map(x -> x.toString()).reduce((e1, e2) -> e1 + "\n" + e2));
                    }
                });
    }

    private Toolbox add(Tipper<? extends PsiElement> t) {
        tipperMap.putIfAbsent(t.getPsiClass(), new LinkedList<>());
        operableTypes.add(t.getPsiClass());
        tipperMap.get(t.getPsiClass()).add(t);
        return this;
    }

    public boolean isElementOfOperableType(PsiElement e) {
        return operableTypes.stream().anyMatch(t -> t.isAssignableFrom(e.getClass()));
    }

    @SuppressWarnings("unchecked")
    public Toolbox executeAllTippers(PsiElement e) {
        if (checkExcluded(e.getContainingFile()) || !isElementOfOperableType(e))
            return this;
        tipperMap.get(type.of(e)).stream().filter(tipper -> tipper.canTip(e)).findFirst()
                .ifPresent(t -> t.tip(e).go(new PsiRewrite().psiFile(e.getContainingFile()).project(e.getProject())));
        return this;
    }

    /**
     * Can element by spartanized
     *
     * @param e JD
     * @return true iff there exists a tip that tip.canTip(element) is true
     */
    public boolean canTip(PsiElement e) {
        return (!checkExcluded(e.getContainingFile()) && canTipType(type.of(e)) && tipperMap.get(type.of(e)).stream().anyMatch(tip -> tip.canTip(e)));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Tipper getTipper(PsiElement e) {
        try {
            if (!checkExcluded(e.getContainingFile()) && canTipType(type.of(e)) &&
                    tipperMap.get(type.of(e)).stream().anyMatch(tip -> tip.canTip(e)))
                return tipperMap.get(type.of(e)).stream().filter(tip -> tip.canTip(e)).findFirst().get();
        } catch (Exception ignore) {
        }
        return new NoTip<>();
    }

    public boolean checkExcluded(PsiFile f) {
        return f == null || excludedFiles.contains(f.getVirtualFile());
    }

    public void excludeFile(PsiFile f) {
        excludedFiles.add(f.getVirtualFile());
    }

    public void includeFile(PsiFile f) {
        excludedFiles.remove(f.getVirtualFile());
    }

    private boolean canTipType(Class<? extends PsiElement> c) {
        return tipperMap.keySet().stream().anyMatch(x -> x.equals(c));
    }

    /**
     * Called on INTELLIJ initialization
     */
    @Override
    public void initComponent() {
        initializeInstance();
        logger.info("Initialized toolbox component");
    }

    @Override
    public void disposeComponent() {
        logger.info("Disposed toolbox component");
    }

    @NotNull
    @Override
    public String getComponentName() {
        return auxGetComponentName();
    }
}
