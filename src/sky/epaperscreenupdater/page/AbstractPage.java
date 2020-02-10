package sky.epaperscreenupdater.page;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import sky.epaperscreenupdater.Logger;
import sky.epaperscreenupdater.Screen;

public abstract class AbstractPage implements Page
{
    protected final Screen screen;
    protected final Page parentPage;
    public static final Font FREDOKA_ONE_FONT;

    static
    {
        Font font=null;
        try(FileInputStream inputStream=new FileInputStream(new File("FredokaOne-Regular.ttf")))
        {
            font=Font.createFont(Font.TRUETYPE_FONT,inputStream);
            Logger.LOGGER.info("Font loaded successfully");
        }
        catch(IOException|FontFormatException e)
        {
            Logger.LOGGER.error("Unable to load the font ("+e.toString()+")");
            e.printStackTrace();
        }
        FREDOKA_ONE_FONT=font;
    }

    protected AbstractPage(Page parentPage)
    {
        screen=new Screen();
        this.parentPage=parentPage;
    }

    public Page getParentPage()
    {
        return parentPage;
    }

    public Screen getScreen()
    {
        return screen;
    }
}
