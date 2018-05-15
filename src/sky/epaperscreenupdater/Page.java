package sky.epaperscreenupdater;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Page affichable sur l'écran e-paper. Cette page peut être une page autonome
 * d'informations ou bien une page de menu contenant à son tour des sous-pages.
 * La hiérarchie d'imbrication est infinie.
 * @author PJ Skyman
 */
public interface Page
{
    public static final DecimalFormat INTEGER_FORMAT=new DecimalFormat("###0");
    public static final DecimalFormat DECIMAL_0_FORMAT=new DecimalFormat("###0.0");
    public static final DecimalFormat DECIMAL_00_FORMAT=new DecimalFormat("###0.00");
    public static final DecimalFormat DECIMAL_000_FORMAT=new DecimalFormat("###0.000");
    public static final DateFormat SHORT_TIME_FORMAT=new SimpleDateFormat("HH:mm");

    /**
     * Retourne le nom d'usage de cette page, c'est-à-dire son identifiant. Ce
     * nom est affiché dans le bandeau incrusté présentant la page lorsqu'on
     * tourne l'encodeur.
     */
    public String getName();

    /**
     * Retourne la page parente de cette page, ou {@code null} si la page n'a
     * pas de page parente.
     */
    public Page getParentPage();

    /**
     * Retourne l'éventuel rang (1-based) de la sous-page spécifiée, ou
     * {@code -1} pour indiquer que la page spécifiée n'est pas une sous-page de
     * cette page.
     */
    public int rankOf(Page subpage);

    /**
     * Retourne le nombre de sous-pages que cette page gère, ou {@code -1} si
     * cette page est une page simple sans sous-pages.
     */
    public int pageCount();

    /**
     * Lance la procédure de remise à jour potentielle de cette page et de ses
     * éventuelles sous-pages. Chaque page est en mesure de décider si elle se
     * remet effectivement à jour ou pas.
     * @return La page elle-même pour créer des appels chaînés.
     * @see #getPixels() getPixels()
     */
    public Page potentiallyUpdate();

    /**
     * Retourne un objet {@link Pixels} représentant le contenu de cette page.
     * Le contenu de la page doit être généré à l'avance (et régulièrement) par
     * la méthode {@link #potentiallyUpdate() potentiallyUpdate()} ; cette
     * méthode ne fait alors rien de spécial à part retourner l'instance
     * interne de {@link Pixels} mise en cache.
     * @see #potentiallyUpdate() potentiallyUpdate()
     */
    public Pixels getPixels();

    /**
     * Indique à cette page qu'un clic a été effectué.
     * @param initial Indique s'il s'agit du tout premier clic d'arrivée sur la
     * page où s'il s'agit d'un clic ultérieur pouvant alors revêtir un rôle
     * différent.
     */
    public void clicked(boolean initial);

    /**
     * Indique à cette page qu'une rotation a été effectuée.
     * @param rotationDirection Indique le sens de rotation.
     * @return Un booléen indiquant au contexte appelant si l'événement a été
     * consumé par cette page ({@code true}) ou non ({@code false}).
     */
    public boolean rotated(RotationDirection rotationDirection);
}
