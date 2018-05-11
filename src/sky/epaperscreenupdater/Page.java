package sky.epaperscreenupdater;

/**
 * Page affichable sur l'écran e-paper. Cette page peut être une page autonome
 * d'informations ou bien une page de menu contenant à son tour des sous-pages.
 * La hiérarchie d'imbrication est infinie.
 * @author PJ Skyman
 */
public interface Page
{
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
     * remet effectivement à jour ou pas. Elle peut aussi retourner directement
     * son contenu à la volée lors d'appels à la méthode {@link #getPixels()
     * getPixels()}.
     * @return La page elle-même pour créer des appels chaînés.
     * @see #getPixels() getPixels()
     */
    public Page potentiallyUpdate();

    /**
     * Retourne un objet {@link Pixels} représentant le contenu de cette page.
     * Le contenu de la page peut être généré à la volée lors de l'appel à cette
     * méthode, ou bien peut être généré à l'avance (et régulièrement) par la
     * méthode {@link #potentiallyUpdate() potentiallyUpdate()} ; cette méthode
     * ne fait alors rien de spécial à part retourner l'instance interne de
     * {@link Pixels} mise en cache.
     * @see #potentiallyUpdate() potentiallyUpdate()
     */
    public Pixels getPixels();

    /**
     * Indique si cette page nécessite une disposition spécifique pour garantir
     * sa remise à jour fluide à l'écran. une page a besoin d'une telle
     * disposition si son taux de rafraîchissement réel est de l'ordre de la
     * seconde. Nota : Toutes les implémentations de gestionnaire d'écran
     * e-paper ne seront pas forcément en mesure d'exploiter cette
     * caractéristique.
     */
    public boolean hasHighFrequency();

    /**
     * Indique à cette page qu'un clic a été effectué, puis retourne à la page
     * parente ou à l'application hôte (s'il n'y a pas de page parente) la
     * nouvelle page parente, par retour de paramètre.
     * @return {@code this} si cette page doit devenir la nouvelle page parente
     * ou {@link #getParentPage() getParentPage()} si la page parente doit
     * rester la même.
     */
    public Page clicked();

    /**
     * Indique à cette page qu'une rotation a été effectuée. Le paramètre
     * {@code rotaryEvent} permet d'avoir des informations sur la rotation, et
     * notamment sa direction.
     */
    public void rotated(RotaryEvent rotaryEvent);
}
