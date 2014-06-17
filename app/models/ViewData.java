package models;


import play.Play;

public class ViewData {

    private play.Logger.ALogger log = play.Logger.of("application");


    public ViewData() throws Exception {

    }

    public String getHtmlHead() {
        return "<!--[if IE 7 ]><html dir=\"ltr\" lang=\"pl-PL\" class=\"no-js ie7\" id=\"top\"><![endif]-->\n" +
                "<!--[if IE 8 ]><html dir=\"ltr\" lang=\"pl-PL\" class=\"no-js ie8\" id=\"top\"><![endif]-->\n" +
                "<!--[if IE 9 ]><html dir=\"ltr\" lang=\"pl-PL\" class=\"no-js ie9\" id=\"top\"><![endif]-->\n" +
                "<!--[if (gt IE 9)|!(IE)]><!--><html dir=\"ltr\" lang=\"pl-PL\" class=\"no-js\" id=\"top\"><!--<![endif]-->";
    }

}
