package com.neko.v2ray


object AppConfig {
    /** UWU **/
    const val UWU_UPDAYE_URL = "https://raw.githubusercontent.com/Blawuken/Neko_v2rayNG/main/release.json"

    /** The application's package name. */
    const val ANG_PACKAGE = BuildConfig.APPLICATION_ID

    /** Directory names used in the app's file system. */
    const val DIR_ASSETS = "assets"
    const val DIR_BACKUPS = "backups"

    /** Legacy configuration keys. */
    const val ANG_CONFIG = "ang_config"
    const val PREF_INAPP_BUY_IS_PREMIUM = "pref_inapp_buy_is_premium"
    const val PREF_IGNORE_BATTERY_OPTIMIZATION = "pref_ignore_battery_optimization"

    /** Preferences mapped to MMKV storage. */
    const val PREF_SNIFFING_ENABLED = "pref_sniffing_enabled"
    const val PREF_ROUTE_ONLY_ENABLED = "pref_route_only_enabled"
    const val PREF_PER_APP_PROXY = "pref_per_app_proxy"
    const val PREF_PER_APP_PROXY_SET = "pref_per_app_proxy_set"
    const val PREF_BYPASS_APPS = "pref_bypass_apps"
    const val PREF_LOCAL_DNS_ENABLED = "pref_local_dns_enabled"
    const val PREF_FAKE_DNS_ENABLED = "pref_fake_dns_enabled"
    const val PREF_LOCAL_DNS_PORT = "pref_local_dns_port"
    const val PREF_VPN_DNS = "pref_vpn_dns"
    const val PREF_ROUTING_DOMAIN_STRATEGY = "pref_routing_domain_strategy"
    const val PREF_ROUTING_MODE = "pref_routing_mode"
    const val PREF_V2RAY_ROUTING_AGENT = "pref_v2ray_routing_agent"
    const val PREF_V2RAY_ROUTING_DIRECT = "pref_v2ray_routing_direct"
    const val PREF_V2RAY_ROUTING_BLOCKED = "pref_v2ray_routing_blocked"
    const val PREF_ROUTING_CUSTOM = "pref_routing_custom"
    const val PREF_MUX_ENABLED = "pref_mux_enabled"
    const val PREF_MUX_CONCURRENCY = "pref_mux_concurrency"
    const val PREF_MUX_XUDP_CONCURRENCY = "pref_mux_xudp_concurrency"
    const val PREF_MUX_XUDP_QUIC = "pref_mux_xudp_quic"
    const val PREF_FRAGMENT_ENABLED = "pref_fragment_enabled"
    const val PREF_FRAGMENT_PACKETS = "pref_fragment_packets"
    const val PREF_FRAGMENT_LENGTH = "pref_fragment_length"
    const val PREF_FRAGMENT_INTERVAL = "pref_fragment_interval"
    const val SUBSCRIPTION_AUTO_UPDATE = "pref_auto_update_subscription"
    const val SUBSCRIPTION_AUTO_UPDATE_INTERVAL = "pref_auto_update_interval"
    const val SUBSCRIPTION_DEFAULT_UPDATE_INTERVAL = "1440" // Default is 24 hours
    const val SUBSCRIPTION_UPDATE_TASK_NAME = "subscription_updater"
    const val PREF_SPEED_ENABLED = "pref_speed_enabled"
    const val PREF_CONFIRM_REMOVE = "pref_confirm_remove"
    const val PREF_START_SCAN_IMMEDIATE = "pref_start_scan_immediate"
    const val PREF_LANGUAGE = "pref_language"

    const val PREF_PREFER_IPV6 = "pref_prefer_ipv6"
    const val PREF_PROXY_SHARING = "pref_proxy_sharing_enabled"
    const val PREF_ALLOW_INSECURE = "pref_allow_insecure"
    const val PREF_SOCKS_PORT = "pref_socks_port"
    const val PREF_HTTP_PORT = "pref_http_port"

    const val PREF_REMOTE_DNS = "pref_remote_dns"
    const val PREF_DOMESTIC_DNS = "pref_domestic_dns"
    const val PREF_DELAY_TEST_URL = "pref_delay_test_url"
    const val PREF_LOGLEVEL = "pref_core_loglevel"
    const val PREF_MODE = "pref_mode"

    /** Cache keys. */
    const val CACHE_SUBSCRIPTION_ID = "cache_subscription_id"
    const val CACHE_KEYWORD_FILTER = "cache_keyword_filter"

    /** Protocol identifiers. */
    const val PROTOCOL_HTTP: String = "http://"
    const val PROTOCOL_HTTPS: String = "https://"
    const val PROTOCOL_FREEDOM: String = "freedom"

    /** Broadcast actions. */
    const val BROADCAST_ACTION_SERVICE = "com.neko.v2ray.action.service"
    const val BROADCAST_ACTION_ACTIVITY = "com.neko.v2ray.action.activity"
    const val BROADCAST_ACTION_WIDGET_CLICK = "com.neko.v2ray.action.widget.click"

    /** Tasker extras. */
    const val TASKER_EXTRA_BUNDLE = "com.twofortyfouram.locale.intent.extra.BUNDLE"
    const val TASKER_EXTRA_STRING_BLURB = "com.twofortyfouram.locale.intent.extra.BLURB"
    const val TASKER_EXTRA_BUNDLE_SWITCH = "tasker_extra_bundle_switch"
    const val TASKER_EXTRA_BUNDLE_GUID = "tasker_extra_bundle_guid"
    const val TASKER_DEFAULT_GUID = "Default"

    /** Tags for different proxy modes. */
    const val TAG_PROXY = "proxy"
    const val TAG_DIRECT = "direct"
    const val TAG_BLOCKED = "block"
    const val TAG_FRAGMENT = "fragment"

    /** Network-related constants. */
    const val UPLINK = "uplink"
    const val DOWNLINK = "downlink"

    /** URLs for various resources. */
    const val androidpackagenamelistUrl =
        "https://raw.githubusercontent.com/2dust/androidpackagenamelist/master/proxy.txt"
    const val v2rayCustomRoutingListUrl =
        "https://raw.githubusercontent.com/2dust/v2rayCustomRoutingList/master/"
    const val v2rayNGUrl = "https://github.com/Blawuken/Neko_v2rayNG"
    const val v2rayNGIssues = "$v2rayNGUrl/issues"
    const val v2rayNGWikiMode = "$v2rayNGUrl/wiki/Mode"
    const val v2rayNGPrivacyPolicy = "https://raw.githubusercontent.com/Blawuken/Neko_v2rayNG/main/CR.md"
    const val PromotionUrl = "https://9.234456.xyz/abc.html?t=1703789826882"
    const val GeoUrl = "https://github.com/malikshi/v2ray-rules-dat/releases/latest/download/"
    const val TgChannelUrl = "https://t.me/uwuresourceguide"
    const val DelayTestUrl = "https://www.gstatic.com/generate_204"
    const val DelayTestUrl2 = "https://www.google.com/generate_204"

    /** DNS server addresses. */
    const val DNS_PROXY = "1.1.1.1"
    const val DNS_DIRECT = "223.5.5.5"
    const val DNS_VPN = "1.1.1.1"

    /** Ports and addresses for various services. */
    const val PORT_LOCAL_DNS = "10853"
    const val PORT_SOCKS = "10808"
    const val PORT_HTTP = "10809"
    const val WIREGUARD_LOCAL_ADDRESS_V4 = "172.16.0.2/32"
    const val WIREGUARD_LOCAL_ADDRESS_V6 = "2606:4700:110:8f81:d551:a0:532e:a2b3/128"
    const val WIREGUARD_LOCAL_MTU = "1420"

    /** Message constants for communication. */
    const val MSG_REGISTER_CLIENT = 1
    const val MSG_STATE_RUNNING = 11
    const val MSG_STATE_NOT_RUNNING = 12
    const val MSG_UNREGISTER_CLIENT = 2
    const val MSG_STATE_START = 3
    const val MSG_STATE_START_SUCCESS = 31
    const val MSG_STATE_START_FAILURE = 32
    const val MSG_STATE_STOP = 4
    const val MSG_STATE_STOP_SUCCESS = 41
    const val MSG_STATE_RESTART = 5
    const val MSG_MEASURE_DELAY = 6
    const val MSG_MEASURE_DELAY_SUCCESS = 61
    const val MSG_MEASURE_CONFIG = 7
    const val MSG_MEASURE_CONFIG_SUCCESS = 71
    const val MSG_MEASURE_CONFIG_CANCEL = 72

    /** Notification channel IDs and names. */
    const val RAY_NG_CHANNEL_ID = "RAY_NG_M_CH_ID"
    const val RAY_NG_CHANNEL_NAME = "V2rayNG Background Service"
    const val SUBSCRIPTION_UPDATE_CHANNEL = "subscription_update_channel"
    const val SUBSCRIPTION_UPDATE_CHANNEL_NAME = "Subscription Update Service"

    /** Protocols Scheme **/
    const val VMESS = "vmess://"
    const val CUSTOM = ""
    const val SHADOWSOCKS = "ss://"
    const val SOCKS = "socks://"
    const val VLESS = "vless://"
    const val TROJAN = "trojan://"
    const val WIREGUARD = "wireguard://"
}
