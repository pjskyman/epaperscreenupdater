package sky.epaperscreenupdater;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import sky.epaperscreenupdater.page.AbstractPage;
import sky.epaperscreenupdater.page.Page;

public class IncrustGenerator
{
    private final Page page;

    public IncrustGenerator(Page page)
    {
        this.page=page;
    }

    public Screen generateStandardIncrust()
    {
        return generateIncrust(false,false);
    }

    public Screen generateOutMessageIncrust()
    {
        return generateIncrust(false,true);
    }

    private Screen generateIncrust(boolean empty,boolean outMessage)
    {
        try
        {
            BufferedImage image=new BufferedImage(296,128,BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D g2d=image.createGraphics();
            g2d.setColor(Color.GRAY);
            g2d.fillRect(0,0,296,128);
            if(!empty)
            {
                g2d.setColor(Color.BLACK);
                Font baseFont=AbstractPage.FREDOKA_ONE_FONT.deriveFont(40f);
                Font descriptionFont=baseFont.deriveFont(20f);
                g2d.setFont(baseFont);
                Page parentPage=page.getParentPage();
                String string1;
                if(outMessage)
                    string1="Sortir de";//on suppose que outMessage n'est à true qu'en cas de menu
                else
                {
                    string1=page.getPageCount()==-1?"Page":"Menu";
                    if(parentPage!=null)
                        string1="["+parentPage.getRankOf(page)+"] "+string1;
                }
                int string1Width=(int)Math.ceil(baseFont.getStringBounds(string1,g2d.getFontRenderContext()).getWidth());
                int string1Height=(int)Math.ceil(baseFont.getStringBounds(string1,g2d.getFontRenderContext()).getHeight());
                g2d.setFont(descriptionFont);
                String string2=page.getName();
                if(outMessage)
                    string2+=" ?";
                int string2Width=(int)Math.ceil(descriptionFont.getStringBounds(string2,g2d.getFontRenderContext()).getWidth());
                int string2Height=(int)Math.ceil(descriptionFont.getStringBounds(string2,g2d.getFontRenderContext()).getHeight());
                int maxStringWidth=Math.max(string1Width,string2Width)+20;
                int totalStringHeight=string1Height+string2Height+10;
                g2d.fillRoundRect(148-maxStringWidth/2,64-totalStringHeight/2,maxStringWidth,totalStringHeight,40,40);
                g2d.setColor(Color.WHITE);
                g2d.setFont(baseFont);
                g2d.drawString(string1,148-string1Width/2,64-totalStringHeight/2+string1Height-7);
                g2d.setFont(descriptionFont);
                g2d.drawString(string2,148-string2Width/2,64-totalStringHeight/2+string1Height-3+string2Height);
            }
            g2d.dispose();
//            try(OutputStream outputStream=new FileOutputStream(new File("incrust.png")))
//            {
//                ImageIO.write(sourceImage,"png",outputStream);
//            }
            return new Screen().setImage(image);
        }
        catch(Exception e)
        {
            Logger.LOGGER.error("Unknown error");
            e.printStackTrace();
            return new Screen();
        }
    }
}
