package com.jwkj.activity;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yoosee.R;
import com.jwkj.adapter.SearchListAdapter;
import com.jwkj.global.Constants;
import com.jwkj.utils.Utils;
import com.jwkj.widget.SortBar;
import com.jwkj.widget.SortBar.OnTouchSortListener;

public class SearchListActivity extends BaseActivity {
	private ExpandableListView eListView;
	private SortBar sort_bar;
	private List<String> data;
	private SearchListAdapter adapter;
	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_list);
		mContext = this;
		initComponent();
	}

	public void initComponent() {

		eListView = (ExpandableListView) findViewById(R.id.search_list);
		sort_bar = (SortBar) findViewById(R.id.sort_bar);

		if (Utils.isZh(mContext)) {
			adapter = new SearchListAdapter(this, data_zh);
		} else {
			adapter = new SearchListAdapter(this, data_en);
		}

		eListView.setAdapter(adapter);
		for (int i = 0, length = adapter.getGroupCount(); i < length; i++) {
			eListView.expandGroup(i);
		}

		sort_bar.setOnTouchSortListener(new OnTouchSortListener() {

			View layoutView = LayoutInflater.from(SearchListActivity.this)
					.inflate(R.layout.dialog_search_list_prompt, null);
			TextView text = (TextView) layoutView.findViewById(R.id.content);
			PopupWindow popupWindow;

			public void onTouchAssortListener(String str) {
				int index = adapter.getAssort().getHashList().indexOfKey(str);
				if (index != -1) {
					eListView.setSelectedGroup(index);
					;
				}
				if (popupWindow != null) {
					text.setText(str);
				} else {
					popupWindow = new PopupWindow(layoutView, 80, 80, false);
					popupWindow.showAtLocation(getWindow().getDecorView(),
							Gravity.CENTER, 0, 0);
				}
				text.setText(str);
			}

			public void onTouchAssortUP() {
				if (popupWindow != null)
					popupWindow.dismiss();
				popupWindow = null;
			}
		});

	}

	private static String[] data_zh = new String[] {
			// 第二排16个 重复国码
			"阿尔巴尼亚 :355", "阿尔及利亚 :213", "阿富汗 :93", "阿根廷 :54", "阿拉伯联合酋长国 :971",
			"阿鲁巴 :297", "阿曼 :968", "阿塞拜疆 :994", "阿森松岛 :247", "埃及 :20",
			"埃塞俄比亚 :251", "爱尔兰 :353", "爱沙尼亚 :372", "安道尔 :376", "安哥拉 :244",
			"安圭拉 :1264", "安提瓜和巴布达 :1268", "奥地利 :43", "奥兰群岛 :358", "澳大利亚 :61",
			"澳门 :853", "巴巴多斯 :1246", "巴布亚新几内亚 :675", "巴哈马 :1242", "巴基斯坦 :92",
			"巴拉圭 :595", "巴勒斯坦 :970", "巴林 :973", "巴拿马 :507", "巴西 :55",
			"白俄罗斯 :375", "百慕大 :1441", "保加利亚 :359", "美国 :1", "贝宁 :229",
			"比利时 :32", "冰岛 :354", "玻利维亚 :591", "波多黎各 :1787", "波兰 :48",
			"波斯尼亚和黑塞哥维那 :387", "博茨瓦纳 :267", "伯利兹 :501", "不丹 :975",
			"布基纳法索 :226", "布隆迪 :257", "朝鲜 :850", "赤道几内亚 :240", "丹麦 :45",
			"德国 :49", "东帝汶 :670", "多哥 :228", "多米尼加共和国 :1809", "多米尼克 :1767",
			"俄罗斯 :7", "厄瓜多尔 :593", "厄立特里亚 :291", "法国 :33", "法罗群岛 :298",
			"法属波利尼西亚 :689", "法属圭亚那 :594", "法属圣马丁 :590", "菲律宾 :63", "佛得角 :238",
			"福克兰群岛 :500", "冈比亚 :220", "刚果（布） :242", "刚果（金） :243", "哥伦比亚 :57",
			"哥斯达黎加 :506", "格林纳达 :1473", "格陵兰 :299", "格鲁吉亚 :995", "根西岛 :44",
			"古巴 :53", "关岛 :1671", "圭亚那 :592", "海地 :509", "韩国 :82", "荷兰 :31",
			"荷兰加勒比 :599", "荷属圣马丁 :1721", "黑山共和国 :382", "洪都拉斯 :504",
			"基里巴斯 :686", "吉布提 :253", "吉尔吉斯斯坦 :996", "几内亚 :224", "几内亚比绍 :245",
			"加纳 :233", "加蓬 :241", "柬埔寨 :855", "捷克共和国 :420", "津巴布韦 :263",
			"喀麦隆 :237", "卡塔尔 :974", "开曼群岛 :1345", "科摩罗 :269", "科特迪瓦 :225",
			"科威特 :965", "克罗地亚 :385", "肯尼亚 :254", "库克群岛 :682", "拉脱维亚 :371",
			"莱索托 :266", "老挝 :856", "黎巴嫩 :961", "利比里亚 :231", "利比亚 :218",
			"立陶宛 :370", "列支敦士登 :423", "留尼汪 :262", "卢森堡 :352", "卢旺达 :250",
			"罗马尼亚 :40", "马达加斯加 :261", "马耳他 :356", "马尔代夫 :960", "马拉维 :265",
			"马来西亚 :60", "马里 :223", "马其顿 :389", "马绍尔群岛 :692", "马提尼克 :596",
			"毛里求斯 :230", "毛里塔尼亚 :222", "美属萨摩亚 :1684", "美属维京群岛 :1340",
			"蒙古 :976", "蒙塞拉特 :1664", "孟加拉国 :880", "秘鲁 :51", "密克罗尼西亚联邦 :691",
			"缅甸 :95", "摩尔多瓦 :373", "摩洛哥 :212", "摩纳哥 :377", "莫桑比克 :258",
			"墨西哥 :52", "纳米比亚 :264", "南非 :27", "南苏丹 :211", "尼泊尔 :977",
			"尼加拉瓜 :505", "尼日尔 :227", "尼日利亚 :234", "纽埃 :683", "挪威 :47",
			"诺福克岛 :6723", "帕劳 :680", "葡萄牙 :351", "日本:81", "瑞典 :46", "瑞士 :41",
			"萨尔瓦多 :503", "萨摩亚 :685", "塞尔维亚 :381", "塞拉利昂 :232", "塞内加尔 :221",
			"塞浦路斯 :357", "塞舌尔 :248", "沙特阿拉伯 :966", "圣多美和普林西比 :239",
			"圣赫勒拿 :290", "圣基茨和尼维斯 :1869", "圣卢西亚 :1758", "圣马力诺 :378",
			"圣皮埃尔和密克隆群岛 :508", "圣文森特和格林纳丁斯 :1784", "斯里兰卡 :94", "斯洛伐克 :421",
			"斯洛文尼亚 :386", "斯威士兰 :268", "苏丹 :249", "苏里南 :597", "索马里 :252",
			"所罗门群岛 :677", "塔吉克斯坦 :992", "台湾 :886", "泰国 :66", "坦桑尼亚 :255",
			"汤加 :676", "特克斯和凯科斯群岛 :1649", "特立尼达和多巴哥 :1868", "突尼斯 :216",
			"图瓦卢 :688", "土耳其 :90", "土库曼斯坦 :993", "托克劳 :690", "瓦利斯和富图纳 :681",
			"瓦努阿图 :678", "危地马拉 :502", "委内瑞拉 :58", "文莱 :673", "乌干达 :256",
			"乌克兰 :380", "乌拉圭 :598", "乌兹别克斯坦 :998", "西班牙 :34", "希腊 :30",
			"香港:852", "新加坡 :65", "新喀里多尼亚 :687", "新西兰 :64", "匈牙利 :36",
			"叙利亚 :963", "牙买加 :1876", "亚美尼亚 :374", "也门 :967", "伊拉克 :964",
			"伊朗 :98", "以色列 :972", "意大利 :39", "印度 :91", "印度尼西亚 :62",
			"英属维京群岛 :1284", "英属印度洋领地 :246", "约旦 :962", "越南 :84", "赞比亚 :260",
			"乍得 :235", "直布罗陀 :350", "智利 :56", "中非共和国 :236", "中国:86", "瑙鲁 :674",
			"梵蒂冈 :379", "斐济 :679",

			"芬兰 :358", "瓜德罗普岛 :590", "哈萨克斯坦 :7", "加拿大 :1", "科科斯群岛 :61",
			"库拉索 :599", "马约特 :262", "曼岛 :44", "北马里亚纳群岛 :1", "圣巴泰勒米 :590",
			"圣诞岛 :61", "斯瓦尔巴特和扬马延 :47", "特里斯坦-达库尼亚群岛 :290", "西撒哈拉 :212",
			"英国 :44", "泽西岛 :44",

	};

	private static String[] data_en = new String[] {
			// 第二排16个 重复国码
			"Albania:355", "Algeria:213", "Afghanistan:93", "Argentina:54",
			"United Arab Emirates:971", "Aruba:297", "Oman:968",
			"Azerbaijan:994", "Ascension:247", "Egypt:20", "Ethiopia:251",
			"Ireland:353", "Estonia:372", "Andorra:376", "Angola:244",
			"Anguilla:1264", "Antigua and Barbuda:1268", "Austria:43",
			"Aland Islands:358", "Australia:61", "Macao:853", "Barbados:1246",
			"Papua New Guinea:675", "Bahamas:1242", "Pakistan:92",
			"Paraguay:595", "Palestine:970", "Bahrain:973", "Panama:507",
			"Brazil:55", "Belarus:375", "Bermuda:1441", "Bulgarian:359",
			"America:1", "Benin:229", "Belgium:32", "Iceland:354",
			"Bolivia:591", "Puerto Rico:1787", "Poland:48",
			"Bosnia and Herzegovina:387", "Botswana:267", "Belize:501",
			"Bhutan:975", "Burkina Faso:226", "Burundi:257", "Korea:850",
			"Equatorial Guinea:240", "Denmark:45", "Germany:49",
			"East Timor:670", "Togo:228", "Dominican Republic:1809",
			"Dominica:1767", "Russia:7", "Ecuador:593", "Eritrea:291",
			"France:33", "Faroe Islands:298", "French Polynesia:689",
			"French Guiana:594", "French St. Martin:590", "Philippines:63",
			"Cape Verde:238", "Falkland Islands:500", "Gambia:220",
			"Congo (Brazzaville):242", "Congo (DRC):243", "Colombia:57",
			"Costa Rica:506", "Grenada:1473", "Greenland:299", "Georgia:995",
			"Guernsey:44", "Cuba:53", "Guam:1671", "Guyana:592", "Haiti:509",
			"Korea:82", "Netherlands:31", "Dutch Caribbean:599",
			"Saint Martin:1721", "Montenegro:382", "Honduras:504",
			"Kiribati:686", "Djibouti:253", "Kirghizstan:996", "Guinea:224",
			"Guinea-Bissau:245", "Ghana:233", "Gabon:241", "Cambodia:855",
			"Czech Republic:420", "Zimbabwe:263", "Cameroon:237", "Qatar:974",
			"Cayman Islands:1345", "Comoros:269", "Côte d'Ivoire:225",
			"Kuwait:965", "Croatia:385", "Kenya:254", "Cook Islands:682",
			"Latvia:371", "Lesotho:266", "Laos:856", "Lebanon:961",
			"Liberia:231", "Libya:218", "Lithuania:370", "Liechtenstein:423",
			"Reunion:262", "Luxembourg:352", "Rwanda:250", "Romania:40",
			"Madagascar:261", "Malta:356", "Maldives:960", "Malawi:265",
			"Malaysia:60", "Mali:223", "Macedonia:389",
			"The Marshall Islands:692", "Martinique:596", "Mauritius:230",
			"Mauritania:222", "Samoa:1684", "U.S. Virgin Islands:1340",
			"Mongolia:976", "Monserrate:1664", "Bangladesh:880", "Peru:51",
			"Federated States of Micronesia:691", "Myanmar:95", "Moldova:373",
			"Morocco:212", "Monaco:377", "Mozambique:258", "Mexico:52",
			"Namibia:264", "South Africa:27", "South Sudan:211", "Nepal:977",
			"Nicaragua:505", "Niger:227", "Nigeria:234", "Niue:683",
			"Norway:47", "Norfolk Island:6723", "Palau:680", "Portugal:351",
			"Japan:81", "Sweden:46", "Switzerland:41", "El Salvador:503",
			"Samoa:685", "Serbia:381", "Sierra Leone:232", "Senegal:221",
			"Cyprus:357", "Seychelles:248", "Saudi Arabia:966",
			"Sao Tome and Principe:239", "St. Helena:290",
			"Saint Kitts and Nevis:1869", "St. Lucia:1758", "San Marino:378",
			"Saint Pierre and Miquelon:508",
			"Saint Vincent and the Grenadines:1784", "Sri Lanka:94",
			"Slovakia:421", "Slovenia:386", "Swaziland:268", "Sudan:249",
			"Surinam:597", "Somalia:252", "Solomon Islands:677",
			"Tajikistan:992", "Taiwan:886", "Thailand:66", "Tanzania:255",
			"Tonga:676", "Turks and Caicos Islands:1649",
			"Trinidad and Tobago:1868", "Tunisia:216", "Tuvalu:688",
			"Turkey:90", "Turkmenistan:993", "Tokelau:690",
			"Wallis and Futuna:681", "Vanuatu:678", "Guatemala:502",
			"Venezuela:58", "Brunei:673", "Uganda:256", "Ukraine:380",
			"Uruguay:598", "Uzbekistan:998", "Spain:34", "Greece:30",
			"Hong Kong:852", "Singapore:65", "New Caledonia:687",
			"New Zealand:64", "Hungary:36", "Syria:963", "Jamaica:1876",
			"Armenia:374", "Yemen:967", "Iraq:964", "Iran:98", "Israel:972",
			"Italy:39", "India:91", "Indonesia:62",
			"British Virgin Islands:1284",
			"British Indian Ocean Territory:246", "Jordan:962", "Vietnam:84",
			"Zambia:260", "Chad:235", "Gibraltar:350", "Chile:56",
			"Central African Republic:236", "China:86", "Nauru:674",
			"The Vatican:379", "Fiji:679",

			"Finland:358", "Guadeloupe:590", "Kazakhstan:7", "Canada:1",
			"Cocos Islands:61", "Curacao:599", "Mayotte:262", "Isle of Man:44",
			"Northern Mariana Islands:1", "Saint Barthelemy:590",
			"Christmas Island:61", "Svalbard and Jan Mayen:47",
			"Tristan - da Cunha:290", "Western Sahara:212", "Britain:44",
			"Jersey:44",

	};

	/*
	 * 
	 * private static String[] data_zh = new String[]{
	 * "马来西亚|60","菲律宾|63","泰国|66","日本|81","越南|84","中国香港特别行政区|852","柬埔寨|855",
	 * "中国|86"
	 * ,"孟加拉国|880","印度|91","阿富汗|93","缅甸|95","黎巴嫩|961","叙利亚|963","科威特|965",
	 * "阿曼|968"
	 * ,"巴林|973","不丹|975","尼泊尔|977","印度尼西亚|62","新加坡|65","文莱|673","韩国|82",
	 * "朝鲜|850","中国澳门特别行政区|853","老挝|856","台湾|886","土耳其|90","巴基斯坦|92","斯里兰卡|94",
	 * "马尔代夫|960"
	 * ,"约旦|962","伊拉克|964","沙特阿拉伯|966","阿拉伯联合酋长国|971","以色列|972","卡塔尔|974"
	 * ,"蒙古|976",
	 * "伊朗|98","俄罗斯|7","荷兰|31","法国|33","直布罗陀|350","卢森堡|352","冰岛|354","马耳他|356",
	 * "芬兰|358","匈牙利|336","南斯拉夫|338","圣马力诺|223","罗马尼亚|40","列支敦士登|4175","英国|44",
	 * "瑞典|46"
	 * ,"波兰|48","希腊|30","比利时|32","西班牙|34","葡萄牙|351","爱尔兰|353","阿尔巴尼亚|355",
	 * "塞浦路斯|357"
	 * ,"保加利亚|359","德国|349","意大利|39","梵蒂冈|396","瑞士|41","奥地利|43","丹麦|45",
	 * "挪威|47","美国|1","中途岛|1808","威克岛|1808","维尔京群岛|1809","波多黎各|1809","巴哈马|1809",
	 * "阿拉斯加|1907"
	 * ,"加拿大|1","夏威夷|1808","安圭拉岛|1809","圣卢西亚|1809","牙买加|1809","巴巴多斯|1809",
	 * "格陵兰岛|299"
	 * ,"福克兰群岛|500","危地马拉|502","洪都拉斯|504","哥斯达黎加|506","海地|509","墨西哥|52",
	 * "阿根廷|54"
	 * ,"智利|56","委内瑞拉|58","圭亚那|592","法属圭亚那|594","马提尼克|596","乌拉圭|598","伯利兹|501",
	 * "萨尔瓦多|503"
	 * ,"尼加拉瓜|505","巴拿马|507","秘鲁|51","古巴|53","巴西|55","哥伦比亚|57","玻利维亚|591",
	 * "厄瓜多尔|593"
	 * ,"巴拉圭|595","苏里南|597","澳大利亚|61","关岛|671","诺福克岛|6723","瑙鲁|674","所罗门群岛|677",
	 * "斐济|679"
	 * ,"纽埃岛|683","西萨摩亚|685","图瓦卢|688","新西兰|64","科科斯岛|6722","圣诞岛|6724","汤加|676",
	 * "瓦努阿图|678"
	 * ,"科克群岛|682","东萨摩亚|684","基里巴斯|686","埃及|20","阿尔及利亚|213","利比亚|218",
	 * "塞内加尔|221"
	 * ,"马里|223","科特迪瓦|225","尼日尔|227","贝宁|229","利比里亚|231","加纳|233","乍得|235",
	 * "喀麦隆|237"
	 * ,"圣多美|239","赤道几内亚|240","刚果|242","安哥拉|244","阿森松|247","苏丹|249","埃塞俄比亚|251",
	 * "吉布提|253"
	 * ,"坦桑尼亚|255","布隆迪|257","赞比亚|260","留尼旺岛|262","纳米比亚|264","莱索托|266","斯威士兰|268"
	 * , "南非|27","阿鲁巴岛|297","摩洛哥|210","突尼斯|216","冈比亚|220","毛里塔尼亚|222","几内亚|224",
	 * "布基拉法索|226",
	 * "多哥|228","毛里求斯|230","塞拉利昂|232","尼日利亚|234","中非|236","佛得角|238",
	 * "普林西比|239","加蓬|241",
	 * "扎伊尔|243","几内亚比绍|245","塞舌尔|248","卢旺达|250","索马里|252","肯尼亚|254"
	 * ,"乌干达|256","莫桑比克|258",
	 * "马达加斯加|261","津巴布韦|263","马拉维|265","博茨瓦纳|267","科摩罗|269"
	 * ,"圣赫勒拿|290","法罗群岛|298" };
	 * 
	 * private static String[] data_en = new String[]{ "Malaysia|60",
	 * "Filipino|63", "Thailand|66", "Japanese|81", "Vietnam|84",
	 * "Hong Kong|852", "Cambodia|855", "China|86", "Bangladesh|880",
	 * "Indian|91", "Afghanistan|93", "Myanmar|95", "Lebanon|961", "Syria|963",
	 * "Kuwait|965", "Oman|968", "Bahrain|973", "Bhutan|975", "Nepal|977",
	 * "Indonesia|62", "Singapore|65", "Brunei|673", "South Korea|82",
	 * "North Korea|850", "Macao|853", "Laos|856", "Taiwan|886", "Turkey|90",
	 * "Pakistan|92", "Sri Lanka|94", "Maldives|960", "Jordan|962", "Iraq|964",
	 * "Saudi Arabia|966","United Arab Emirates|971", "Israel|972", "Qatar|974",
	 * "Mongolia|976", "Iran|98", "Russia|7", "Netherlands|31", "France|33",
	 * "Gibraltar|350", "Luxembourg|352", "Iceland|354", "Malta|356",
	 * "Finland|358", "Hungarian|336", "Yugoslavia|338", "San Marino|223",
	 * "Romania|40", "Liechtenstein|4175", "English|44", "Sweden|46",
	 * "Poland|48", "Greece|30", "Belgium|32", "Spanish|34", "Portugal|351",
	 * "Ireland|353", "Albania|355", "Cyprus|357", "Bulgarian|359",
	 * "German|349", "Italian|39", "Vatican|396", "Switzerland|41",
	 * "Austria|43", "Denmark|45", "Norway|47", "USA|1", "Midway|1808",
	 * "Wake Island|1808", "Virgin Islands|1809", "Puerto Rico|1809",
	 * "The Bahamas|1809", "Alaska|1907", "Canada|1", "Hawaii|1808",
	 * "Anguilla|1809", "Saint Lucia|1809", "Jamaica|1809", "Barbados|1809",
	 * "Greenland|299", "Falkland Islands|500", "Guatemala|502", "Honduras|504",
	 * "Costa Rica|506", "Haiti|509", "Mexico|52", "Argentina|54", "Chile|56",
	 * "Venezuela|58", "Guyana|592", "French Guiana|594", "Martinique|596",
	 * "Uruguay|598", "Belize|501", "El Salvador|503", "Nicaragua|505",
	 * "Panama|507", "Peru|51", "Cuba|53", "Brazil|55", "Colombia|57",
	 * "Bolivia|591", "Ecuador|593", "Paraguay|595", "Suriname|597",
	 * "Australia|61", "Guam|671", "Norfolk Island|6723", "Nauru|674",
	 * "Solomon Islands|677", "Fiji|679", "Niue|683", "Western Samoa|685",
	 * "Tuvalu|688", "New Zealand|64", "Cocos Island|6722",
	 * "Christmas Island|6724", "Tonga|676", "Vanuatu|678", "Cook Islands|682",
	 * "East Samoa|684", "Kiribati|686", "Egypt|20", "Algeria|213", "Libya|218",
	 * "Senegal|221", "Mali|223", "Côte d'Ivoire|225", "Niger|227", "Benin|229",
	 * "Liberia|231", "Ghana|233", "Chad|235", "Cameroon|237", "São Tomé|239",
	 * "Equatorial Guinea|240", "Congo|242", "Angola|244", "Ascension|247",
	 * "Sudan|249", "Ethiopia|251", "Djibouti|253", "Tanzania|255",
	 * "Burundi|257", "Zambia|260", "Reunion|262", "Namibia|264", "Lesotho|266",
	 * "Swaziland|268", "South Africa|27", "Aruba|297", "Morocco|210",
	 * "Tunisia|216", "Gambia|220", "Mauritania|222", "Guinea|224",
	 * "Buji La France Search|226", "Togo|228", "Mauritius|230",
	 * "Sierra Leone|232", "Nigeria|234", "Africa|236", "Cape Verde|238",
	 * "Tome and Principe|239", "Gabon|241", "Zaire|243", "Guinea-Bissau|245",
	 * "Seychelles|248", "Rwanda|250", "Somalia|252", "Kenya|254", "Uganda|256",
	 * "Mozambique|258", "Madagascar|261", "Zimbabwe|263", "Malawi|265",
	 * "Botswana|267", "Comoros|269", "St. Helena|290", "Faroe Islands|298" };
	 */

	public static String getNameByCode(Context context, int code) {
		String name = "";
		String[] searchData;
		if (Utils.isZh(context)) {
			searchData = data_zh;
		} else {
			searchData = data_en;
		}

		for (String data : searchData) {
			String[] info = data.split(":");
			if (code == Integer.parseInt(info[1])) {
				name = info[0];
				break;
			}
		}

		return name;
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_SEARCHLISTACTIVITY;
	}
}
