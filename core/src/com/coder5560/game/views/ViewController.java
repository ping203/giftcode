package com.coder5560.game.views;

import imp.view.ViewLogin;
import utils.factory.AppPreference;
import utils.factory.PlatformResolver;
import utils.factory.StringSystem;
import utils.networks.FacebookConnector;
import utils.networks.UserInfo;
import utils.screen.AbstractGameScreen;
import utils.screen.GameCore;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.enums.GameEvent;
import com.coder5560.game.enums.ViewState;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.screens.GameScreen;

public class ViewController implements IViewController {
	public Stage				stage;
	public Array<IViews>		views;

	public IViews				currentView;
	public FacebookConnector	facebookConnector;
	private GameCore			_gameParent;
	private AbstractGameScreen	_gameScreen;
	public PlatformResolver		platformResolver;
	boolean						reset			= false;
	private float				timeToReload	= 0;

	public ViewController(GameCore _gameParent, AbstractGameScreen gameScreen) {
		super();
		this._gameParent = _gameParent;
		this._gameScreen = gameScreen;
		AppPreference.instance.load();
	}

	public void build(Stage stage) {
		this.stage = stage;
		views = new Array<IViews>();
		ViewLogin viewLogin = new ViewLogin();
		viewLogin.build(stage, this, StringSystem.VIEW_LOGIN, new Rectangle(0,
				0, Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));
		viewLogin.buildComponent();
		viewLogin.show(new OnCompleteListener() {

			@Override
			public void onError() {
			}

			@Override
			public void done() {
			}
		});
	}

	public void buidTest(Stage stage) {
		// this.stage = stage;
		// views = new Array<IViews>();
		// ViewTestList viewTestList = new ViewTestList();
		// viewTestList.build(getStage(), this, StringSystem.VIEW_HOME,
		// new Rectangle(0, 0, Constants.WIDTH_SCREEN,
		// Constants.HEIGHT_SCREEN - Constants.HEIGHT_ACTIONBAR));
		// viewTestList.buildComponent();
		// viewTestList.show(null);
	}

	@Override
	public void update(float delta) {
		updateDataThread(delta);
		try {
			for (int i = 0; i < views.size; i++) {
				views.get(i).update(delta);
				if (views.get(i).getViewState() == ViewState.DISPOSE) {
					removeView(views.get(i).getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (reset) {
			UserInfo.getInstance().getPermission().resetPermission();
			for (Actor actor : stage.getActors()) {
				if (actor instanceof View) {
					stage.getActors().removeValue(actor, false);
				}
			}
			TraceView.instance.traceView.clear();
			views.clear();

			ViewLogin viewLogin = new ViewLogin();
			viewLogin.build(stage, this, StringSystem.VIEW_LOGIN,
					new Rectangle(0, 0, Constants.WIDTH_SCREEN,
							Constants.HEIGHT_SCREEN));
			viewLogin.buildComponent();
			viewLogin.show(new OnCompleteListener() {

				@Override
				public void onError() {
				}

				@Override
				public void done() {
				}
			});
			reset = false;
		}

	}

	public void updateDataThread(float delta) {

	}

	@Override
	public boolean isContainView(String name) {
		if (avaiable()) {
			for (int i = 0; i < views.size; i++) {
				if (views.get(i).getName().equalsIgnoreCase(name))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean addView(IViews view) {
		if (!avaiable())
			return false;
		if (isContainView(view.getName()))
			return false;
		views.add(view);
		return true;
	}

	@Override
	public void removeView(String name) {
		if (!avaiable())
			return;
		if (!isContainView(name))
			return;
		IViews view = getView(name);
		if (view == null)
			return;
		view.destroyComponent();
		views.removeValue(view, false);
		stage.getActors().removeValue((Actor) view, true);
	}

	@Override
	public void toFront(String name) {
		if (isContainView(name)) {
			((Actor) getView(name)).toFront();
			if (isContainView(StringSystem.VIEW_MAIN_MENU))
				((Actor) getView(StringSystem.VIEW_MAIN_MENU)).toFront();
			if (isContainView(StringSystem.VIEW_ACTION_BAR))
				((Actor) getView(StringSystem.VIEW_ACTION_BAR)).toFront();
		}
	}

	@Override
	public IViews getView(String name) {
		for (int i = 0; i < views.size; i++) {
			if (views.get(i).getName().equalsIgnoreCase(name)) {
				return views.get(i);
			}
		}
		return null;
	}

	@Override
	public Array<IViews> getViews() {
		if (avaiable())
			return views;
		return null;
	}

	public boolean avaiable() {
		return views != null && stage != null;
	}

	@Override
	public Stage getStage() {
		return stage;
	}

	public void setFacebookConnector(FacebookConnector facebookConnector) {
		this.facebookConnector = facebookConnector;
	}

	public FacebookConnector getFacebookConnector() {
		return facebookConnector;
	}

	@Override
	public void setGameParent(GameCore gameParent) {
		this._gameParent = gameParent;
	}

	@Override
	public GameCore getGameParent() {
		return _gameParent;
	}

	// this method will sort that all of our view from a container of view.
	@Override
	public void sortView() {
	}

	@Override
	public IViews getCurrentView() {
		return currentView;
	}

	@Override
	public void setCurrentView(IViews view) {
		this.currentView = view;
		TraceView.instance.addViewToTrace(view.getName());
	}

	@Override
	public void resetAll() {
		reset = true;
	}

	@Override
	public GameScreen getGameScreen() {
		if (_gameScreen instanceof GameScreen) {
			return (GameScreen) _gameScreen;
		}
		return null;
	}

	public PlatformResolver getPlatformResolver() {
		return platformResolver;
	}

	public void setPlatformResolver(PlatformResolver platformResolver) {
		this.platformResolver = platformResolver;
	}

	@Override
	public void notifyEvent(GameEvent gameEvent) {
		for (int i = 0; i < views.size; i++) {
			views.get(i).onGameEvent(gameEvent);
		}
	}

	@Override
	public void resetHome() {
		for (int i = 0; i < views.size; i++) {
			if (!(views.get(i).getName()
					.equalsIgnoreCase(StringSystem.VIEW_HOME)
					|| views.get(i).getName()
							.equalsIgnoreCase(StringSystem.VIEW_ACTION_BAR) || views
					.get(i).getName()
					.equalsIgnoreCase(StringSystem.VIEW_MAIN_MENU))) {
				views.get(i).setViewState(ViewState.DISPOSE);
			}
		}
		getView(StringSystem.VIEW_HOME).show(null);
	}

}
