package imp.view;

import java.util.ArrayList;

import utils.elements.ItemDatePartner;
import utils.elements.PartnerPicker;
import utils.elements.PartnerSelectBox;
import utils.factory.AppPreference;
import utils.factory.DateTime;
import utils.factory.Factory;
import utils.factory.FontFactory.FontType;
import utils.factory.Style;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.networks.UserInfo;
import utils.screen.AbstractGameScreen;
import utils.screen.Toast;

import com.aia.appsreport.component.list.ItemList;
import com.aia.appsreport.component.list.ListDetail;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.enums.RoleID;
import com.coder5560.game.enums.ViewState;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.CustomTextField;
import com.coder5560.game.ui.DatePicker;
import com.coder5560.game.ui.ItemListener;
import com.coder5560.game.ui.LabelButton;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.ui.PageV2;
import com.coder5560.game.views.View;

public class ViewLogGiftCodeDetail extends View {

	PageV2 pages;
	ListDetail listDetail;
	boolean isLoad = true;

	DatePicker dateFrom;
	DatePicker dateTo;
	CustomTextField tfSearch;
	PartnerPicker partner;
	PartnerPicker partnerFun;
	TextButton btnXem;
	Group gExtendDate;
	boolean isExtend = false;

	boolean isLoadByName;
	String responseByName;
	boolean isLoadByRoleId;
	String reponseByRoleId;

	boolean isChange;
	String stateofpartnerFun = "0";
	String laststateofpartnerFun = "0";

	Image iconextendDate;
	PartnerSelectBox quickDatePicker;
	boolean isChangeFun;
	int stateFun;

	int roldId = -1;
	String username = "";

	Color color_text = Constants.COLOR_ACTIONBAR;
	Color color_white = Color.WHITE;
			

	public ViewLogGiftCodeDetail buildComponent() {
		roldId = UserInfo.getInstance().getRoleId();
		username = AppPreference.instance.name;
		top();
		Image bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch,
				new Color(255 / 255f, 255 / 255f, 255 / 255f, 1)));
		bg.setSize(getWidth(), getHeight());
		addActor(bg);

		Group gTop = new Group();
		gTop.setSize(getWidth(), 160);
		Image bgTop = new Image(new NinePatch(
				Assets.instance.ui.reg_ninepatch3, 6, 6, 6, 6));
		bgTop.setSize(getWidth() + 4, gTop.getHeight());
		bgTop.setColor(color_white);
		Image bgDatetime = new Image(new NinePatch(
				Assets.instance.ui.reg_ninepatch, new Color(color_white)));
		bgDatetime.setSize(getWidth(), 60);
		bgDatetime.setPosition(0, gTop.getHeight() - bgDatetime.getHeight());
		dateFrom = new DatePicker();
		dateTo = new DatePicker();
		dateFrom.setDate(DateTime.getDateBefor("dd", 7),
				DateTime.getDateBefor("MM", 7),
				DateTime.getDateBefor("yyyy", 7));
		dateTo.setDate(DateTime.getCurrentDate("dd"),
				DateTime.getCurrentDate("MM"), DateTime.getCurrentDate("yyyy"));
		dateFrom.setSize(Constants.WIDTH_SCREEN / 2 - 5, 50);
		dateTo.setSize(Constants.WIDTH_SCREEN / 2 - 5, 50);
		dateFrom.setStartString("");
		dateTo.setStartString("");
		dateFrom.setPosition(30, bgTop.getHeight() - dateFrom.getHeight() - 5);
		dateTo.setPosition(dateFrom.getX() + dateFrom.getWidth() - 25,
				bgTop.getHeight() - dateTo.getHeight() - 5);
		final Image bgiconextendDate = new Image(new NinePatch(
				Assets.instance.ui.reg_ninepatch));
		bgiconextendDate.setSize(50, 50);
		bgiconextendDate.setPosition(getWidth() - bgiconextendDate.getWidth(),
				dateFrom.getY());
		bgiconextendDate.setColor(color_white);
		iconextendDate = new Image(new TextureRegion(new Texture(
				Gdx.files.internal("Img/play.png"))));
		iconextendDate.setSize(25, 25);
		iconextendDate.setOrigin(iconextendDate.getWidth() / 2,
				iconextendDate.getHeight() / 2);
		iconextendDate.setColor(Color.GRAY);
		iconextendDate.getColor().a = 0.5f;
		iconextendDate.setPosition(
				getWidth() - iconextendDate.getWidth() - 10,
				dateFrom.getY() + dateFrom.getHeight() / 2
						- iconextendDate.getHeight() / 2);
		iconextendDate.setRotation(270);
		bgiconextendDate.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				iconextendDate.getColor().a = 0.25f;
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				iconextendDate.getColor().a = 0.5f;
				if (isExtend) {
					gExtendDate.clearActions();
					iconextendDate.clearActions();
					gExtendDate.addAction(Actions.moveTo(0, dateFrom.getY(),
							0.15f));
					gExtendDate.addAction(Actions.alpha(0, 0.075f));
					iconextendDate.addAction(Actions.rotateTo(270, 0.15f));
					isExtend = false;
				} else {
					gExtendDate.clearActions();
					iconextendDate.clearActions();
					gExtendDate.addAction(Actions.moveTo(0, dateFrom.getY()
							- gExtendDate.getHeight() - 5, 0.15f));
					gExtendDate.addAction(Actions.alpha(1, 0.075f));
					iconextendDate.addAction(Actions.rotateTo(90, 0.15f));
					isExtend = true;
				}
				super.touchUp(event, x, y, pointer, button);
			}
		});
		iconextendDate.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				iconextendDate.getColor().a = 0.25f;
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				iconextendDate.getColor().a = 0.5f;
				if (isExtend) {
					gExtendDate.clearActions();
					iconextendDate.clearActions();
					gExtendDate.addAction(Actions.moveTo(0, dateFrom.getY(),
							0.15f));
					gExtendDate.addAction(Actions.alpha(0, 0.075f));
					iconextendDate.addAction(Actions.rotateTo(270, 0.15f));
					isExtend = false;
				} else {
					gExtendDate.clearActions();
					iconextendDate.clearActions();
					gExtendDate.addAction(Actions.moveTo(0, dateFrom.getY()
							- gExtendDate.getHeight() - 5, 0.15f));
					gExtendDate.addAction(Actions.alpha(1, 0.075f));
					iconextendDate.addAction(Actions.rotateTo(90, 0.15f));
					isExtend = true;
				}
				super.touchUp(event, x, y, pointer, button);
			}
		});
		gExtendDate = new Group();
		gExtendDate.setSize(getWidth(), 90);
		gExtendDate.setTransform(true);
		Image bgExtendDate = new Image(new NinePatch(
				Assets.instance.ui.reg_ninepatch));
		bgExtendDate.setColor(color_white);
		bgExtendDate.setSize(gExtendDate.getWidth(), gExtendDate.getHeight());
		Label lbExtendDate = new Label("Chọn nhanh", Style.ins.getLabelStyle(
				20, FontType.Regular, Color.BLACK));
		quickDatePicker = new PartnerSelectBox(Style.ins.selectBoxStyle);
		quickDatePicker.setSize(220, 40);
		quickDatePicker.addPartner(new ItemDatePartner(0));
		quickDatePicker.addPartner(new ItemDatePartner(1));
		quickDatePicker.addPartner(new ItemDatePartner(2));
		quickDatePicker.addPartner(new ItemDatePartner(3));
		quickDatePicker.addPartner(new ItemDatePartner(7));
		quickDatePicker.addPartner(new ItemDatePartner(14));
		quickDatePicker.addPartner(new ItemDatePartner(21));
		quickDatePicker.addPartner(new ItemDatePartner(28));
		quickDatePicker.setSelectedIndex(4);
		quickDatePicker.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				gExtendDate.clearActions();
				iconextendDate.clearActions();
				gExtendDate.addAction(Actions.moveTo(0, dateFrom.getY(), 0.15f));
				gExtendDate.addAction(Actions.alpha(0, 0.075f));
				iconextendDate.addAction(Actions.rotateTo(270, 0.15f));
				isExtend = false;
				dateFrom.setDate(DateTime
						.getDateBefor("dd", ((ItemDatePartner) quickDatePicker
								.getSelected()).daybefor), DateTime
						.getDateBefor("MM", ((ItemDatePartner) quickDatePicker
								.getSelected()).daybefor), DateTime
						.getDateBefor("yyyy",
								((ItemDatePartner) quickDatePicker
										.getSelected()).daybefor));
				dateTo.setDate(DateTime.getCurrentDate("dd"),
						DateTime.getCurrentDate("MM"),
						DateTime.getCurrentDate("yyyy"));
			}

		});
		LabelButton btnOk = new LabelButton("Chọn", Style.ins.getLabelStyle(20,
				FontType.Regular, Color.BLACK), 80, 40, LabelButton.CENTER);
		lbExtendDate.setPosition(55,
				gExtendDate.getHeight() / 2 - lbExtendDate.getHeight() / 2);
		quickDatePicker.setPosition(
				lbExtendDate.getX() + lbExtendDate.getWidth() + 25,
				gExtendDate.getHeight() / 2 - quickDatePicker.getHeight() / 2);
		btnOk.setPosition(getWidth() - btnOk.getWidth() - 20,
				quickDatePicker.getY());
		btnOk.setListener(new ItemListener() {

			@Override
			public void onItemClick() {
				// TODO Auto-generated method stub
				gExtendDate.clearActions();
				iconextendDate.clearActions();
				gExtendDate.addAction(Actions.moveTo(0, dateFrom.getY(), 0.15f));
				gExtendDate.addAction(Actions.alpha(0, 0.075f));
				iconextendDate.addAction(Actions.rotateTo(270, 0.15f));
				isExtend = false;
				dateFrom.setDate(DateTime
						.getDateBefor("dd", ((ItemDatePartner) quickDatePicker
								.getSelected()).daybefor), DateTime
						.getDateBefor("MM", ((ItemDatePartner) quickDatePicker
								.getSelected()).daybefor), DateTime
						.getDateBefor("yyyy",
								((ItemDatePartner) quickDatePicker
										.getSelected()).daybefor));
				dateTo.setDate(DateTime.getCurrentDate("dd"),
						DateTime.getCurrentDate("MM"),
						DateTime.getCurrentDate("yyyy"));
			}
		});
		gExtendDate.addActor(bgExtendDate);
		gExtendDate.addActor(lbExtendDate);
		gExtendDate.addActor(quickDatePicker);
		// gExtendDate.addActor(btnOk);

		gExtendDate.setPosition(0, dateFrom.getY());
		gExtendDate.getColor().a = 0f;

		partnerFun = new PartnerPicker(Style.ins.selectBoxStyle);
		partnerFun.setSize(2 * getWidth() / 5, 38);
		partnerFun.setPosition(getWidth() / 2 - partnerFun.getWidth() / 2,
				dateFrom.getY() - 5 - partnerFun.getHeight());
		if (UserInfo.getInstance().getRoleId() == RoleID.AGENCY_LEVEL2) {
			partnerFun.addPartner(0, "Cá nhân", "1");
			stateofpartnerFun = "-1";
		} else if (UserInfo.getInstance().getRoleId() == RoleID.USER_MANAGER) {
			partnerFun.addPartner(0, username, "4");
			stateofpartnerFun = "4";
		} else {
			stateofpartnerFun = "0";
			partnerFun.addPartner(0, "Tất cả", "0");
			partnerFun.addPartner(1, "Cá nhân", "1");
			partnerFun.addPartner(3, "Xem theo SDT", "2");
			partnerFun.addPartner(2, "Xem theo cấp", "3");
		}
		TextFieldStyle style = new TextFieldStyle();
		NinePatch ninepatch = new NinePatch(
				Assets.instance.getRegion("ninepatch_stock"), 4, 4, 4, 4);
		style.background = new NinePatchDrawable(ninepatch);
		style.background.setLeftWidth(10);
		style.cursor = new NinePatchDrawable(new NinePatch(
				Assets.instance.getRegion("bg_white"), Color.GRAY));
		style.font = Assets.instance.fontFactory.getFont(20, FontType.Regular);
		style.fontColor = Color.BLACK;

		tfSearch = new CustomTextField("", style);
		tfSearch.setOnscreenKeyboard(AbstractGameScreen.keyboard);
		tfSearch.setMessageText("Số điện thoại");
		tfSearch.setSize(2 * getWidth() / 5, 40);
		tfSearch.setPosition(getWidth() / 2 - tfSearch.getWidth() / 2,
				dateTo.getY() - tfSearch.getHeight() - 5);
		tfSearch.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// AbstractGameScreen.keyboard.registerTextField(tfSearch,
				// "tfSearch", KeyboardConfig.NORMAL,
				// KeyboardConfig.SINGLE_LINE);
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		partner = new PartnerPicker(Style.ins.selectBoxStyle);
		partner.setSize(2 * getWidth() / 5, 38);
		partner.setPosition(getWidth() / 2 - partner.getWidth() / 2,
				dateTo.getY() - 5 - partner.getHeight());
		btnXem = new TextButton("Xem", Style.ins.textButtonStyle);
		btnXem.setSize(2 * getWidth() / 5, 40);
		btnXem.setPosition(getWidth() / 2 - btnXem.getWidth() / 2,
				bgTop.getY() + 10);
		btnXem.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (stateofpartnerFun.equals("1")) {
					Request.getInstance().getLogMoneyGiftCodeByName(
							AppPreference.instance.name, dateFrom.getDate(),
							dateTo.getDate(), new getListByName());
					Loading.ins.show(ViewLogGiftCodeDetail.this);
				} else if (stateofpartnerFun.equals("4")) {
					Request.getInstance().getLogMoneyGiftCodeByName(username,
							dateFrom.getDate(), dateTo.getDate(),
							new getListByName());
					Loading.ins.show(ViewLogGiftCodeDetail.this);
				} else if (stateofpartnerFun.equals("0")) {
					Request.getInstance().getLogMoneyGiftCodeByRole(
							AppPreference.instance.name, dateFrom.getDate(),
							dateTo.getDate(), partner.getPartnerId(),
							new getListByRoleId());
					Loading.ins.show(ViewLogGiftCodeDetail.this);
				} else if (stateofpartnerFun.equals("2")) {
					Request.getInstance().getLogMoneyGiftCodeByName(
							tfSearch.getText(), dateFrom.getDate(),
							dateTo.getDate(), new getListByName());
					Loading.ins.show(ViewLogGiftCodeDetail.this);
				} else if (stateofpartnerFun.equals("3")) {
					Request.getInstance().getLogMoneyGiftCodeByRole(
							AppPreference.instance.name, dateFrom.getDate(),
							dateTo.getDate(), partner.getPartnerId(),
							new getListByRoleId());
					Loading.ins.show(ViewLogGiftCodeDetail.this);
				}
				AbstractGameScreen.keyboard.hide();
				super.clicked(event, x, y);
			}
		});
		gTop.addActor(bgTop);
		gTop.addActor(tfSearch);
		gTop.addActor(partner);
		gTop.addActor(partnerFun);
		gTop.addActor(btnXem);
		gTop.addActor(gExtendDate);
		gTop.addActor(bgDatetime);
		gTop.addActor(dateFrom);
		gTop.addActor(dateTo);
		gTop.addActor(bgiconextendDate);
		gTop.addActor(iconextendDate);

		tfSearch.setVisible(false);
		partner.setVisible(false);

		add(gTop).width(gTop.getWidth()).padLeft(-3).row();
		pages = new PageV2(getWidth(), 50);
		pages.init();
		pages.setListener(new ItemListener() {

			@Override
			public void onItemClick() {
				// TODO Auto-generated method stub
				listDetail.addAction(Actions.sequence(
						Actions.alpha(0, 0.2f, Interpolation.exp5Out),
						Actions.run(new Runnable() {

							@Override
							public void run() {
								listDetail.setScrollX(0);
								listDetail.setScrollY(0);
								listDetail.table.clear();
								loadListDetail();
							}
						}), Actions.alpha(1, 0.2f, Interpolation.exp5Out)));

			}
		});
		pages.setPosition(0, 0);
		listDetail = new ListDetail(new Table(), new Rectangle(0,
				pages.getHeight(), getWidth(), getHeight() - gTop.getHeight()
						- pages.getHeight()));
		add(listDetail).width(listDetail.getWidth())
				.height(listDetail.getHeight()).row();
		add(pages).width(getWidth());

		for (int i = 0; i < UserInfo.getInstance().getListPartners().size(); i++) {
			partner.addPartner(
					UserInfo.getInstance().getListPartners().get(i).id,
					UserInfo.getInstance().getListPartners().get(i).title,
					UserInfo.getInstance().getListPartners().get(i).code);
		}
		return this;
	}

	public void setUserName(String name) {
		this.username = name;
		partnerFun.getSelected().name = name;
	}

	public void setDate(String date) {
		dateFrom.setDate(date);
		dateTo.setDate(date);
	}

	public void setFun(int select) {
		if (UserInfo.getInstance().getRoleId() == 3
				|| UserInfo.getInstance().getRoleId() == 4) {
			partnerFun.setSelectedIndex(0);
		} else {
			partnerFun.setSelectedIndex(1);
		}
	}

	// 841278426508
	void loadListByName() {
		if (isLoadByName) {
			JsonValue json = (new JsonReader()).parse(responseByName);
			boolean result = json.getBoolean(ExtParamsKey.RESULT);
			String message = json.getString(ExtParamsKey.MESSAGE);

			if (result) {
				pages.removeAllPage();
				listDetail.table.clear();
				JsonValue arr = json.get(ExtParamsKey.LIST);
				if (arr.size > 0) {
					for (int i = 0; i < arr.size; i++) {
						JsonValue content = arr.get(i);
						String agencyname = content
								.getString(ExtParamsKey.AGENCY_NAME);
						int type = content.getInt(ExtParamsKey.ACTIVITY_TYPE);
						long money_before = content
								.getLong(ExtParamsKey.MONEY_BEFORE);
						String str_money_before = Factory
								.getDotMoney((long) money_before);
						long money_after = content
								.getLong(ExtParamsKey.MONEY_AFTER);
						String str_money_after = Factory
								.getDotMoney((long) money_after);
						long money_change = Math.abs(content
								.getLong(ExtParamsKey.MONEY_CHANGE));
						String str_money_change = Factory
								.getDotMoney((long) money_change);
						String currency = content
								.getString(ExtParamsKey.CURRENCY);
						String date = DateTime.getStringDate(
								content.getLong(ExtParamsKey.GEN_DATE),
								"HH:mm:ss dd-MM-yyyy");
						long time = content.getLong(ExtParamsKey.GEN_DATE);

						String full_name = content
								.getString(ExtParamsKey.FULL_NAME);
						String code_id = content
								.getString(ExtParamsKey.CODE_ID);

						ArrayList<String> data = new ArrayList<String>();
						data.add(agencyname);
						data.add(full_name);
						data.add(type + "");
						data.add(str_money_before);
						data.add(str_money_after);
						data.add(str_money_change);
						data.add(currency);
						data.add(date);
						data.add(code_id);
						data.add(DateTime.getCusDate(time));
						pages.addData(data);
					}
					pages.init();
					loadListDetail();
				} else {
					Toast.makeText(getStage(), "Không có dữ liệu !!!", 3f);
				}
			} else {
				Toast.makeText(getStage(), message, 3f);
			}
			Loading.ins.hide();
			isLoadByName = false;
		}
	}

	void loadListDetail() {
		for (int i = 0; i < pages.getCurrentDataPage().size(); i++) {
			ArrayList<String> data = pages.getCurrentDataPage().get(i);
			String loaigd = "tạo GiftCode";
			if (data.get(4).equals("1")) {
				loaigd = "trả GiftCode";
			}
			ItemList itemList = new ItemList(listDetail, listDetail.getWidth(),
					80);
			itemList.btnExpand.setY(itemList.item.getY() + 15);
			itemList.btnExpand.setX(itemList.btnExpand.getX() - 5);
			Image iconUser = new Image(Assets.instance.ui.getIconUser());
			iconUser.setSize(18, 18);
			Image icon_coin = new Image(new Texture(
					Gdx.files.internal("Img/coin.png")));
			icon_coin.setSize(18, 18);
			// itemList.addComponent(iconUser, 13, 70);
			itemList.addComponent(
					new Label(data.get(1) + " - " + data.get(0), Style.ins
							.getLabelStyle(22, FontType.Regular, Color.WHITE)),
					40, 45);
			// itemList.addComponent(icon_coin, 13, 40);
			itemList.addComponent(
					new Label(data.get(5) + " " + data.get(6), Style.ins
							.getLabelStyle(18, FontType.Regular, Color.WHITE)),
					40, 15);
			Label lbTime = new Label(data.get(9), Style.ins.getLabelStyle(18,
					FontType.Regular, Color.WHITE));
			itemList.addComponent(lbTime,
					itemList.getWidth() - lbTime.getWidth() - 10, 45);
			itemList.addSubItem(
					new Label("Họ tên", Style.ins.getLabelStyle(15,
							FontType.Light, Color.BLACK)), itemList.getWidth(),
					25);
			itemList.addSubItem(
					new Label(data.get(1), Style.ins.getLabelStyle(18,
							FontType.Light, Constants.COLOR_ACTIONBAR)),
					itemList.getWidth(), 25);
			itemList.addSubItem(
					new Label("Số điện thoại", Style.ins.getLabelStyle(15,
							FontType.Light, Color.BLACK)), itemList.getWidth(),
					25);
			itemList.addSubItem(
					new Label(data.get(0), Style.ins.getLabelStyle(18,
							FontType.Light, Constants.COLOR_ACTIONBAR)),
					itemList.getWidth(), 25);
			itemList.addSubItem(new Label("Số tiền trước khi " + loaigd,
					Style.ins.getLabelStyle(15, FontType.Light, Color.BLACK)),
					itemList.getWidth(), 25);
			itemList.addSubItem(
					new Label(data.get(3) + " " + data.get(6), Style.ins
							.getLabelStyle(18, FontType.Light,
									Constants.COLOR_ACTIONBAR)), itemList
							.getWidth(), 25);
			itemList.addSubItem(new Label("Số tiền sau khi " + loaigd,
					Style.ins.getLabelStyle(15, FontType.Light, Color.BLACK)),
					itemList.getWidth(), 25);
			itemList.addSubItem(
					new Label(data.get(4) + " " + data.get(6), Style.ins
							.getLabelStyle(18, FontType.Light,
									Constants.COLOR_ACTIONBAR)), itemList
							.getWidth(), 25);
			itemList.addSubItem(
					new Label("Số tiền " + loaigd, Style.ins.getLabelStyle(15,
							FontType.Light, Color.BLACK)), itemList.getWidth(),
					25);
			itemList.addSubItem(
					new Label(data.get(5) + " " + data.get(6), Style.ins
							.getLabelStyle(18, FontType.Light,
									Constants.COLOR_ACTIONBAR)), itemList
							.getWidth(), 25);
			itemList.addSubItem(
					new Label("ID của GiftCode", Style.ins.getLabelStyle(15,
							FontType.Light, Color.BLACK)), itemList.getWidth(),
					25);
			itemList.addSubItem(
					new Label(data.get(8), Style.ins.getLabelStyle(18,
							FontType.Light, Constants.COLOR_ACTIONBAR)),
					itemList.getWidth(), 25);
			itemList.addSubItem(
					new Label("Thời gian", Style.ins.getLabelStyle(15,
							FontType.Light, Color.BLACK)), itemList.getWidth(),
					25);
			itemList.addSubItem(
					new Label(data.get(7), Style.ins.getLabelStyle(18,
							FontType.Light, Constants.COLOR_ACTIONBAR)),
					itemList.getWidth(), 25);

			listDetail.addItemMenu(itemList);
		}
	}

	@Override
	public void update(float delta) {
		if (!partnerFun.getPartnerCode().equals(stateofpartnerFun)) {
			isChange = true;
			stateofpartnerFun = partnerFun.getPartnerCode();
		}
		if (isChange) {
			if (partnerFun.getPartnerCode().equals("0")
					|| partnerFun.getPartnerCode().equals("1")) {
				partnerFun.addAction(Actions.moveTo(
						getWidth() / 2 - partnerFun.getWidth() / 2,
						partnerFun.getY(), 0.2f));
				tfSearch.addAction(Actions.moveTo(
						getWidth() / 2 - tfSearch.getWidth() / 2,
						tfSearch.getY(), 0.2f));
				tfSearch.addAction(Actions.sequence(Actions.alpha(0, 0.2f),
						Actions.run(new Runnable() {

							@Override
							public void run() {
								tfSearch.setVisible(false);
							}
						})));
				partner.addAction(Actions.moveTo(
						getWidth() / 2 - partner.getWidth() / 2,
						tfSearch.getY(), 0.2f));
				partner.addAction(Actions.sequence(Actions.alpha(0, 0.2f),
						Actions.run(new Runnable() {

							@Override
							public void run() {
								partner.setVisible(false);
							}
						})));
				laststateofpartnerFun = partnerFun.getPartnerCode();
			} else if (partnerFun.getPartnerCode().equals("2")) {
				tfSearch.setVisible(true);
				tfSearch.getColor().a = 0;
				partnerFun
						.addAction(Actions.moveTo(30, partnerFun.getY(), 0.2f));
				if (laststateofpartnerFun.equals("0")
						|| laststateofpartnerFun.equals("1")) {
					tfSearch.addAction(Actions.moveTo(
							getWidth() - tfSearch.getWidth() - 30,
							tfSearch.getY(), 0.2f));
					tfSearch.addAction(Actions.alpha(1, 0.2f));
				} else if (laststateofpartnerFun.equals("3")) {
					partner.addAction(Actions.moveTo(30, partner.getY(), 0.2f));
					partner.addAction(Actions.sequence(Actions.alpha(0, 0.1f),
							Actions.run(new Runnable() {

								@Override
								public void run() {
									partner.setVisible(false);
									tfSearch.addAction(Actions.moveTo(
											getWidth() - tfSearch.getWidth()
													- 30, tfSearch.getY(), 0.2f));
									tfSearch.addAction(Actions.alpha(1, 0.2f));
								}
							})));
				}
				laststateofpartnerFun = partnerFun.getPartnerCode();
			} else if (partnerFun.getPartnerCode().equals("3")) {
				partner.setVisible(true);
				partner.getColor().a = 0;
				partnerFun
						.addAction(Actions.moveTo(30, partnerFun.getY(), 0.2f));
				if (laststateofpartnerFun.equals("0")
						|| laststateofpartnerFun.equals("1")) {
					partner.addAction(Actions.moveTo(
							getWidth() - partner.getWidth() - 30,
							tfSearch.getY(), 0.2f));
					partner.addAction(Actions.alpha(1, 0.2f));
				} else if (laststateofpartnerFun.equals("2")) {
					tfSearch.addAction(Actions.moveTo(30, tfSearch.getY(), 0.2f));
					tfSearch.addAction(Actions.sequence(Actions.alpha(0, 0.1f),
							Actions.run(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									tfSearch.setVisible(false);
									partner.addAction(Actions.moveTo(getWidth()
											- partner.getWidth() - 30,
											partner.getY(), 0.2f));
									partner.addAction(Actions.alpha(1, 0.2f));
								}
							})));
				}
				laststateofpartnerFun = partnerFun.getPartnerCode();
			}
			isChange = false;
		}
		loadListByName();
		super.update(delta);
	}

	@Override
	public void show(OnCompleteListener listener) {
		if (UserInfo.getInstance().getRoleId() == 3) {
			Request.getInstance().getLogMoneyGiftCodeByName(
					AppPreference.instance.name, dateFrom.getDate(),
					dateTo.getDate(), new getListByRoleId());
			Loading.ins.show(this);
		} else if (UserInfo.getInstance().getRoleId() == 4) {
			Request.getInstance()
					.getLogMoneyGiftCodeByName(username, dateFrom.getDate(),
							dateTo.getDate(), new getListByRoleId());
			Loading.ins.show(this);
		} else {
			if (partnerFun.getPartnerId() == 1) {
				Request.getInstance().getLogMoneyGiftCodeByName(
						AppPreference.instance.name, dateFrom.getDate(),
						dateTo.getDate(), new getListByRoleId());
			} else {
				Request.getInstance().getLogMoneyGiftCodeByRole(
						AppPreference.instance.name, dateFrom.getDate(),
						dateTo.getDate(), partner.getPartnerId(),
						new getListByRoleId());
			}
			Loading.ins.show(this);
		}
		super.show(listener);
	}

	@Override
	public void hide(OnCompleteListener listener) {
		super.hide(listener);
		setViewState(ViewState.DISPOSE);
	}

	@Override
	public void back() {
		// TODO Auto-generated method stub
		super.back();
		getViewController().removeView(getName());
	}

	@Override
	public String getLabel() {
		return "Lịch sử GiftCode";
	}

	class getListByName implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responseByName = httpResponse.getResultAsString();
			isLoadByName = true;
		}

		@Override
		public void failed(Throwable t) {
			// TODO Auto-generated method stub

		}

		@Override
		public void cancelled() {
			// TODO Auto-generated method stub

		}

	}

	class getListByRoleId implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			// TODO Auto-generated method stub
			responseByName = httpResponse.getResultAsString();
			isLoadByName = true;
		}

		@Override
		public void failed(Throwable t) {
			// TODO Auto-generated method stub

		}

		@Override
		public void cancelled() {
			// TODO Auto-generated method stub
		}
	}
}
