package rs.pedjaapps.KernelTuner.model;

/**
 * Created by pedja on 12/7/13.
 */
public class NavigationMenu
{
    int icon;
    int title;
    int text;

    public NavigationMenu(int title, int text, int icon)
    {
        this.text = text;
        this.title = title;
        this.icon = icon;
    }

    public int getText()
    {
        return text;
    }

    public void setText(int text)
    {
        this.text = text;
    }

    public int getTitle()
    {
        return title;
    }

    public void setTitle(int title)
    {
        this.title = title;
    }

    public int getIcon()
    {
        return icon;
    }

    public void setIcon(int icon)
    {
        this.icon = icon;
    }
}
