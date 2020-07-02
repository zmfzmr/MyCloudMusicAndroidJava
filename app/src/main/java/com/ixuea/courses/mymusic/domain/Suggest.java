package com.ixuea.courses.mymusic.domain;

import java.util.List;

/**
 * 搜索建议模型
 */
public class Suggest {
    private List<SearchTitle> sheets;//歌单搜索建议
    private List<SearchTitle> users;//用户模型搜索建议

    public List<SearchTitle> getSheets() {
        return sheets;
    }

    public void setSheets(List<SearchTitle> sheets) {
        this.sheets = sheets;
    }

    public List<SearchTitle> getUsers() {
        return users;
    }

    public void setUsers(List<SearchTitle> users) {
        this.users = users;
    }
}
