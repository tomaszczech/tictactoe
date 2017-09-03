package com.czecht.tictactoe.web.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.czecht.tictactoe.application.PlayerService;

import lombok.Getter;
import lombok.Setter;

@Scope(value = "session")
@Component(value = "loginController")
@ELBeanName(value = "loginController")
@Join(path = "/", to = "/login.jsf")
public class LoginController {

	private static final String LOGIN_SUCCESS = "/game/player-list.xhtml?faces-redirect=true";

	private final PlayerService playerService;

	@Getter
	@Setter
	private String username;

	@Autowired
	public LoginController(PlayerService playerService) {
		this.playerService = playerService;
	}

	public String login() {
		String user = playerService.loginPlayer(username);
		FacesContext context = FacesContext.getCurrentInstance();

		if(user == null) {
			context.addMessage(null, new FacesMessage("Brak dostepu! Podany gracz juz istnieje."));
			username = null;
			return null;
		} else {
			context.getExternalContext().getSessionMap().put("user", user);
			return LOGIN_SUCCESS;
		}
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		playerService.logoutPlayer(username);
		return "/login.jsf?faces-redirect=true";
	}
}
