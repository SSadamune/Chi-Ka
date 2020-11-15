package com.ssadamune.utils;

import java.util.Map;

public class MyConsts {
    private MyConsts(){}

    public static final String[] TOKYO = { 
        "chiyoda", "chuo", "minato", "shinjuku", "bunkyo", "shibuya", "taito",
        "sumida", "koto", "arakawa", "adachi", "katsushika", "edogawa", "shinagawa",
        "meguro", "ota", "setagaya", "nakano", "suginami", "nerima", "toshima", 
        "kita", "itabashi", "hachioji", "tachikawa", "musashino", "mitaka", "ome"
    };

    public static final Map<String, String> FEATURES = Map.ofEntries(
            // 住所・交通
            Map.entry("２沿線以上利用可", "100"), Map.entry("始発駅", "101"),
            // 住所・便利
            Map.entry("市街地が近い", "111"), Map.entry("スーパー 徒歩10分以内", "112"), Map.entry("総合病院 徒歩10分以内", "113"),
            Map.entry("小学校 徒歩10分以内", "114"),
            // 住所・環境
            Map.entry("都市近郊", "120"), Map.entry("閑静な住宅地", "121"), Map.entry("周辺交通量少なめ", "122"),
            Map.entry("整備された歩道", "123"), Map.entry("緑豊かな住宅地", "124"), Map.entry("リバーサイド", "125"),
            Map.entry("大型タウン内", "126"), Map.entry("避暑地", "127"), Map.entry("海まで2km以内", "128"),
            Map.entry("隣家との間隔が大きい", "129"),
            // 住所・娯楽
            Map.entry("ゴルフ場が近い", "140"), Map.entry("テニスコートが近い", "141"), Map.entry("スキー場が近い", "142"),
            // 住所・地形
            Map.entry("高台に立地", "150"), Map.entry("平坦地", "151"), Map.entry("駅まで平坦", "152"),
            // 住所・土地
            Map.entry("区画整理地内", "160"), Map.entry("開発分譲地内", "161"), Map.entry("整形地", "162"), Map.entry("角地", "163"),
            Map.entry("土地50坪以上", "164"), Map.entry("土地100坪以上", "165"), Map.entry("畑", "170"),

            // 方位・採光
            Map.entry("陽当り良好", "200"), Map.entry("全室２面採光", "201"), Map.entry("３面採光", "202"),
            // 方位・通風
            Map.entry("通風良好", "210"),
            // 方位・視野
            Map.entry("前面棟無", "220"), Map.entry("眺望良好", "221"), Map.entry("山が見える", "222"), Map.entry("湖・池が見える", "223"),
            Map.entry("オーシャンビュー", "224"), Map.entry("花火大会鑑賞", "225"), Map.entry("田園風景", "226"),
            // 方位・南向き
            Map.entry("南向き", "230"), Map.entry("東南向き", "231"), Map.entry("南西向き", "232"), Map.entry("南面バルコニー", "233"),
            Map.entry("全室南向き", "234"), Map.entry("全室南西向き", "235"), Map.entry("全室東南向き", "236"),
            Map.entry("南側道路面す", "237"),
            // 方位・階層
            Map.entry("最上階・上階なし", "240"), Map.entry("高層階", "241"),
            // 方位・角住戸
            Map.entry("角住戸", "250"),

            // 物件・安全防災
            Map.entry("セキュリティ充実", "300"), Map.entry("地盤調査済", "301"), Map.entry("制震・免震・耐震", "302"),
            Map.entry("融雪対策", "303"), Map.entry("耐震基準適合証明書", "304"), Map.entry("設計住宅性能評価書", "305"),
            Map.entry("建設住宅性能評価書（新築時）", "306"), Map.entry("建設住宅性能評価書（既存住宅）", "307"),
            // 物件・駐車駐輪車庫
            Map.entry("駐輪場", "310"), Map.entry("平面駐車場", "311"), Map.entry("自走式駐車場", "312"),
            Map.entry("ハイルーフ駐車場空きあり", "313"), Map.entry("ハイルーフ駐車場", "313"), Map.entry("EV車充電設備", "314"),
            Map.entry("車庫", "315"), Map.entry("シャッター車庫", "316"), Map.entry("地下車庫", "317"), Map.entry("カースペース", "318"),
            Map.entry("カーポート", "319"), Map.entry("ビルトインガレージ", "320"), Map.entry("駐車２台可", "321"),
            Map.entry("駐車３台以上可", "322"),
            // 物件・マンション施設
            Map.entry("宅配ボックス", "330"), Map.entry("エレベーター", "331"), Map.entry("共有施設充実", "332"),
            Map.entry("２４時間有人管理", "333"), Map.entry("キッズルーム・託児所", "334"), Map.entry("バリアフリー", "335"),
            Map.entry("屋上", "336"), Map.entry("温泉付", "337"), Map.entry("共用露天風呂", "338"), Map.entry("露天風呂", "338"),
            Map.entry("プール", "339"),
            // 物件・住宅設備
            Map.entry("高速ネット対応", "340"), Map.entry("BS・CS・CATV", "341"), Map.entry("ＴＶモニタ付インターホン", "342"),
            Map.entry("床暖房", "343"), Map.entry("スマートキー", "344"), Map.entry("高気密高断熱住宅", "345"),
            // 物件・一戸建て
            Map.entry("２階建", "350"), Map.entry("３階建以上", "351"), Map.entry("２世帯住宅", "352"), Map.entry("リビング階段", "353"),
            // 物件・上下水
            Map.entry("公営水道", "360"), Map.entry("公共下水", "361"), Map.entry("本下水", "361"), Map.entry("公共上下水", "362"),
            Map.entry("個別浄化槽", "363"), Map.entry("井戸", "369"),
            // 物件・ガス電力
            Map.entry("オール電化", "370"), Map.entry("東京電力", "371"), Map.entry("太陽光発電システム", "372"),
            Map.entry("東京ガス", "373"), Map.entry("都市ガス", "374"), Map.entry("プロパンガス", "375"), Map.entry("個別LPG", "376"),
            Map.entry("個別プロパンガス", "376"), Map.entry("集中LPG", "377"), Map.entry("集中プロパンガス", "377"),

            // 部屋・リフォーム
            Map.entry("内装リフォーム", "400"), Map.entry("外装リフォーム", "401"), Map.entry("内外装リフォーム", "402"),
            Map.entry("フローリング張替", "403"), Map.entry("修繕・点検の記録", "404"), Map.entry("スケルトン渡し", "405"),
            Map.entry("自然素材使用", "406"), Map.entry("ウッドデッキ", "407"),
            // 部屋・リノベーション
            Map.entry("リノベーション", "410"), Map.entry("複層ガラス", "411"), Map.entry("全居室複層ガラスか複層サッシ", "412"),
            Map.entry("全居室フローリング", "413"), Map.entry("適合リノベーション", "414"), Map.entry("古民家風", "415"),
            // 部屋・室
            Map.entry("和室", "420"), Map.entry("全居室６畳以上", "421"), Map.entry("可動間仕切り", "422"), Map.entry("吹抜け", "423"),
            Map.entry("ロフト", "424"), Map.entry("天井高２．５ｍ以上", "425"), Map.entry("ゲストルーム", "426"),

            // 部屋・LDK
            Map.entry("ＬＤＫ１５畳以上", "440"), Map.entry("ＬＤＫ１８畳以上", "441"), Map.entry("ＬＤＫ２０畳以上", "442"),
            // 部屋・キッチン
            Map.entry("システムキッチン", "450"), Map.entry("対面式キッチン", "451"), Map.entry("食器洗乾燥機", "452"),
            Map.entry("ディスポーザー（生ごみ粉砕処理器）", "453"), Map.entry("浄水器", "454"), Map.entry("ＩＨクッキングヒーター", "455"),
            Map.entry("アイランドキッチン", "456"),
            // 部屋・洗濯洗面浴室
            Map.entry("浴室乾燥機", "460"), Map.entry("省エネ給湯器", "461"), Map.entry("オートバス", "462"),
            Map.entry("シャワー付洗面化粧台", "463"), Map.entry("浴室に窓", "464"), Map.entry("ミストサウナ", "465"),
            Map.entry("ＴＶ付浴室", "466"), Map.entry("ジェットバス", "467"), Map.entry("オーディオバス", "468"),
            Map.entry("浴室１坪以上", "469"),
            // 部屋・トイレ
            Map.entry("温水洗浄便座", "470"), Map.entry("節水型トイレ", "471"), Map.entry("高機能トイレ", "472"),
            Map.entry("トイレ２ヶ所", "473"),
            // 部屋・収納
            Map.entry("全居室収納", "480"), Map.entry("ウォークインクローゼット", "481"), Map.entry("納戸", "482"),
            Map.entry("シューズインクローク", "483"), Map.entry("パントリー（食器・食品の収納庫）", "484"), Map.entry("床下収納", "485"),
            Map.entry("屋根裏収納", "486"),
            // 部屋・バルコニー&庭
            Map.entry("ワイドバルコニー", "490"), Map.entry("ルーフバルコニー", "491"), Map.entry("２面以上バルコニー", "492"),
            Map.entry("テラス", "493"), Map.entry("専用庭", "494"), Map.entry("南庭", "494"), Map.entry("庭", "494"),
            Map.entry("庭１０坪以上", "495"), Map.entry("バルコニー・屋上に水栓あり", "496"), Map.entry("家庭菜園", "497"),
            Map.entry("前道６ｍ以上", "498"),

            // 構造
            Map.entry("木", "500"), Map.entry("軽量鉄骨", "501"), Map.entry("軽量気泡コンクリート", "502"), Map.entry("ALC", "502"),
            Map.entry("鉄骨ALC", "502"), Map.entry("S造(ALC)", "502"), Map.entry("重量鉄骨", "503"), Map.entry("鉄骨", "503"),
            Map.entry("S", "503"), Map.entry("Ｓ", "503"), Map.entry("鉄筋コンクリート", "504"), Map.entry("RC", "504"),
            Map.entry("ＲＣ", "504"), Map.entry("WRC", "505"), Map.entry("鉄骨鉄筋コンクリート", "506"), Map.entry("SRC", "506"),
            Map.entry("ＳＲＣ", "506"), Map.entry("鉄骨プレキャストコンクリート", "507"), Map.entry("HPC", "507"),
            Map.entry("一部木", "510"), Map.entry("一部軽量鉄骨", "511"), Map.entry("一部ALC", "512"), Map.entry("一部鉄骨", "513"),
            Map.entry("一部S", "513"), Map.entry("一部RC", "514"), Map.entry("一部WRC", "515"), Map.entry("一部SRC", "516"),
            Map.entry("一部HPC", "517"), Map.entry("一部コンクリートブロック", "518"),
            // 屋根 https://style.tokyu-resort.co.jp/roof-kind
            Map.entry("陸屋根", "520"), Map.entry("瓦葺", "522"), Map.entry("セメント瓦葺", "523"), Map.entry("亜鉛メッキ鋼板葺", "524"),
            Map.entry("ステンレス鋼板", "525"), Map.entry("ガルバリウム鋼板", "526"), Map.entry("アルミ金属板合金メッキ鋼板茸", "527"),
            Map.entry("銅板葺", "528"), Map.entry("アルミニューム板茸", "529"), Map.entry("カラー鉄板アルミニューム板茸", "530"),
            Map.entry("スレート茸", "531"), Map.entry("石綿セメント茸", "532"), Map.entry("石綿セメント板茸", "532"),
            Map.entry("カラーベスト茸", "533"), Map.entry("コロニアル茸", "533"),
            // 工法

            Map.entry("ログハウス", "590"),

            // 規約
            Map.entry("ペット相談", "600"), Map.entry("バイク置場", "601"), Map.entry("２４時間ゴミ出し可", "602"),
            Map.entry("温泉引き込み可", "603"),

            // その他
            Map.entry("即入居可", "900"), Map.entry("年内入居可", "901"), Map.entry("年度内入居可", "902"),
            Map.entry("フラット３５Sに対応", "910"), Map.entry("フラット３５・S適合証明書", "910"), Map.entry("瑕疵保証付（不動産会社独自）", "920"),
            Map.entry("瑕疵保険（国交省指定）保証利用可", "921"), Map.entry("瑕疵保険（国交省指定）保証付", "921"), Map.entry("新築時・増改築時の設計図", "930"),
            Map.entry("建築確認完了検査済証", "940"), Map.entry("建築士等の建物検査報告書", "941"), Map.entry("長期優良住宅認定通知書", "943"),
            Map.entry("法適合状況調査報告書", "944"), Map.entry("BELS/省エネ基準適合認定書あり", "950"), Map.entry("低炭素住宅", "951"),
            Map.entry("エコポイント対象住宅", "952"), Map.entry("空き家バンク登録物件", "960"), Map.entry("省エネルギー対策", "999"));

}
