package com.example.dellaanjeh.dootapp;

/**
 * Created by dellaanjeh on 9/12/16.
 */
public class NavItem {
    String navTitle;
    String navSubtitle;
    int navIcon;

    public NavItem(String title, String subtitle, int icon) {
        navTitle = title;
        navSubtitle = subtitle;
        navIcon = icon;
    }

    public int getNavIcon() {
        return navIcon;
    }

    public void setNavIcon(int navIcon) {
        this.navIcon = navIcon;
    }

    public String getNavTitle() {
        return navTitle;
    }

    public void setNavTitle(String navTitle) {
        this.navTitle = navTitle;
    }

    public String getNavSubtitle() {
        return navSubtitle;
    }

    public void setNavSubtitle(String navSubtitle) {
        this.navSubtitle = navSubtitle;
    }
}
