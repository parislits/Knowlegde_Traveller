package com.example.paris.knowledge_traveller;

class Wiki {
    private String wikiText;
    private String imgUrl;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getWikiText() {
        return wikiText;
    }

    public void setWikiText(String wikiText) {
        this.wikiText = wikiText;
    }


    @Override
    public String toString() {
        return "Wiki text{" +
                "text=" + wikiText +
                '}';
    }


}
