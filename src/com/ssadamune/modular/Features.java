package com.ssadamune.modular;

import java.util.Map;

public class Features {
    public static final Map<String, Integer> map = Map.ofEntries(
            // 住所・交通
            Map.entry("２沿線以上利用可", 100),
            Map.entry("始発駅", 101),
            // 住所・便利
            Map.entry("市街地が近い", 111),
            Map.entry("スーパー 徒歩10分以内", 112),
            Map.entry("総合病院 徒歩10分以内", 113),
            Map.entry("小学校 徒歩10分以内", 114),
            // 住所・環境
            Map.entry("都市近郊", 120),
            Map.entry("閑静な住宅地", 121),
            Map.entry("周辺交通量少なめ", 122),
            Map.entry("整備された歩道", 123),
            Map.entry("緑豊かな住宅地", 124),
            Map.entry("リバーサイド", 125),
            // 住所・地形
            Map.entry("高台に立地", 130),
            Map.entry("平坦地", 131),
            Map.entry("駅まで平坦", 132),

            // 方位・採光視野通風
            Map.entry("陽当り良好", 200),
            Map.entry("通風良好", 201),
            Map.entry("前面棟無", 202),
            Map.entry("眺望良好", 203),
            Map.entry("２面以上バルコニー", 204),
            Map.entry("全室２面採光", 205),
            Map.entry("３面採光", 206),
            // 方位・南向き
            Map.entry("南向き", 210),
            Map.entry("東南向き", 211),
            Map.entry("南西向き", 212),
            Map.entry("南面バルコニー", 213),
            // 方位・角住戸
            Map.entry("角住戸", 220),
            // 方位・階層
            Map.entry("最上階・上階なし", 230),
            Map.entry("高層階", 231),

            // 物件・安全防災
            Map.entry("セキュリティ充実", 300),
            Map.entry("耐震基準適合証明書", 301),
            Map.entry("制震・免震・耐震", 302),
            Map.entry("設計住宅性能評価書", 303),
            Map.entry("建設住宅性能評価書（新築時）", 304),
            // 物件・外部施設
            Map.entry("宅配ボックス", 320),
            Map.entry("エレベーター", 321),
            Map.entry("駐輪場", 322),
            Map.entry("平面駐車場", 323),
            Map.entry("自走式駐車場", 324),
            Map.entry("バリアフリー", 329),
            // 物件・内部施設
            Map.entry("高速ネット対応", 330),
            Map.entry("BS・CS・CATV", 331),
            Map.entry("ＴＶモニタ付インターホン", 332),
            Map.entry("床暖房", 333),
            // 物件・政策
            Map.entry("ペット相談", 340),
            Map.entry("バイク置場", 341),
            Map.entry("２４時間ゴミ出し可", 342),

            // 部屋・リフォーム
            Map.entry("内装リフォーム", 400),
            Map.entry("外装リフォーム", 401),
            Map.entry("内外装リフォーム", 402),
            Map.entry("フローリング張替", 403),
            Map.entry("修繕・点検の記録", 404),
            // 部屋・リノベーション
            Map.entry("リノベーション", 410),
            Map.entry("複層ガラス", 411),
            Map.entry("全居室複層ガラスか複層サッシ", 412),
            Map.entry("全居室フローリング", 413),
            Map.entry("適合リノベーション", 414),
            // 部屋・間取り
            Map.entry("ＬＤＫ１５畳以上", 420),
            Map.entry("ＬＤＫ２０畳以上", 421),
            Map.entry("全居室６畳以上", 422),
            Map.entry("可動間仕切り", 423),
            Map.entry("ワイドバルコニー", 424),
            // 部屋・収納
            Map.entry("全居室収納", 431),
            Map.entry("ウォークインクローゼット", 432),
            Map.entry("納戸", 433),
            Map.entry("シューズインクローク", 434),
            Map.entry("パントリー（食器・食品の収納庫）", 435),
            // 部屋・キッチン
            Map.entry("システムキッチン", 450),
            Map.entry("対面式キッチン", 451),
            Map.entry("食器洗乾燥機", 452),
            Map.entry("ディスポーザー（生ごみ粉砕処理器）", 453),
            Map.entry("浄水器", 454),
            // 部屋・洗濯洗面浴室
            Map.entry("浴室乾燥機", 460),
            Map.entry("省エネ給湯器", 461),
            Map.entry("オートバス", 462),
            Map.entry("シャワー付洗面化粧台", 463),
            Map.entry("浴室に窓", 464),
            Map.entry("ミストサウナ", 465),
            // 部屋・トイレ
            Map.entry("温水洗浄便座", 470),
            Map.entry("節水型トイレ", 471),
            Map.entry( "高機能トイレ", 472),

            // その他
            Map.entry("即入居可", 900),
            Map.entry("年内入居可", 901),
            Map.entry("年度内入居可", 902),
            Map.entry("フラット３５Sに対応", 910),
            Map.entry("フラット３５・S適合証明書", 910)
            );

}
