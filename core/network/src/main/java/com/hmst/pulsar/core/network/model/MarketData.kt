package com.hmst.pulsar.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Định danh bảng giao dịch */
@Serializable
enum class BoardId(val description: String) {
    UNSPECIFIED("Không xác định"),
    AL("Tất cả bảng"),
    G1("Lô chẵn"),
    G3("Board phiên sau giờ (PLO)"),
    G4("Lô lẻ"),
    T1("Thỏa thuận lô chẵn 9h-14h45"),
    T3("Thỏa thuận lô chẵn 14h45-15h"),
    T4("Thỏa thuận lô lẻ 9h-14h45"),
    T6("Thỏa thuận lô lẻ 14h45-15h"),
}

/** Mã sự kiện chuyển trạng thái phiên giao dịch */
@Serializable
enum class EventId(val description: String) {
    AA1("Bắt đầu phiên khớp lệnh định kỳ mở cửa"),
    AB1("Bắt đầu phiên khớp lệnh liên tục"),
    AB2("Kết thúc giao dịch của bảng"),
    AC2("Thực hiện khớp lệnh định kỳ đóng cửa"),
    AD1("Bắt đầu nhận lệnh"),
    AD2("Thực hiện khớp lệnh định kỳ"),
    AW8("Nghỉ trưa"),
    AW9("Tiếp tục giao dịch sau nghỉ trưa"),
    AX1("Bảng giao dịch bắt đầu hoạt động"),
    BB1("Bắt đầu phiên khớp lệnh liên tục cho chứng khoán thông thường"),
    BC1("Bắt đầu phiên khớp lệnh định kỳ đóng cửa"),
    CC1("Bắt đầu phiên PCA đóng cửa"),
    CD1("Bắt đầu phiên PCA"),
    CD3("Thực hiện khớp lệnh PCA"),
}

/** Loại chỉ số thị trường. Giá trị trên API là [code], ví dụ "001" */
@Serializable
enum class IndexTypeCode(val code: String, val description: String) {
    @SerialName("001")
    VNINDEX("001", "Chỉ số sàn HOSE"),
    @SerialName("002")
    HNX("002", "Chỉ số sàn HNX"),
    @SerialName("100")
    HNX30("100", "Top 30 cổ phiếu sàn HNX"),
    @SerialName("101")
    VN30("101", "Top 30 cổ phiếu sàn HOSE"),
    @SerialName("104")
    VN100("104", "Top 100 cổ phiếu sàn HOSE"),
    @SerialName("151")
    VNXALLSHARE("151", "Chỉ số các cổ phiếu chọn lọc sàn HOSE"),
    @SerialName("301")
    UPCOM("301", "Chỉ số sàn UPCOM"),
    @SerialName("504")
    VNMITECH("504", "Chỉ số nhóm cổ phiếu công nghệ"),
    @SerialName("505")
    VN50GROWTH("505", "Chỉ số nhóm 50 cổ phiếu tăng trưởng sàn HOSE"),
    @SerialName("506")
    VNDIVIDEND("506", "Chỉ số nhóm cổ phiếu có tỷ suất cổ tức tăng trưởng"),
}

/** Mã thị trường */
@Serializable
enum class MarketId(val description: String) {
    UNSPECIFIED("Không xác định"),
    DVX("Phái sinh sàn HNX"),
    HCX("Trái phiếu doanh nghiệp sàn HNX"),
    STO("Cổ phiếu sàn HOSE"),
    STX("Cổ phiếu sàn HNX"),
    UPX("Cổ phiếu sàn Upcom"),
}

/** Tên chỉ số thị trường (IndexName) */
@Serializable
enum class IndexName(val description: String) {
    VNINDEX("Chỉ số sàn HOSE"),
    HNX("Chỉ số sàn HNX"),
    HNX30("Chỉ số Top 30 cổ phiếu sàn HNX"),
    VN30("Chỉ số Top 30 cổ phiếu sàn HOSE"),
    VN100("Chỉ số Top 100 cổ phiếu sàn HOSE"),
    VNXALLSHARE("Chỉ số các cổ phiếu chọn lọc sàn HOSE"),
    UPCOM("Chỉ số sàn UPCOM"),
    VNMITECH("Chỉ số nhóm cổ phiếu công nghệ"),
    VN50GROWTH("Chỉ số nhóm 50 cổ phiếu tăng trưởng sàn HOSE"),
    VNDIVIDEND("Chỉ số nhóm cổ phiếu có tỷ suất cổ tức tăng trưởng"),
}

/** Nhóm sản phẩm theo thị trường */
@Serializable
enum class ProductGrpId(val description: String) {
    UNSPECIFIED("Không xác định"),
    FBX("Hợp đồng tương lai Trái phiếu"),
    FIO("Hợp đồng tương lai Chỉ số"),
    HCX("Trái phiếu Doanh nghiệp HNX"),
    STO("Cổ phiếu sàn HOSE"),
    STX("Cổ phiếu sàn HNX"),
    UPX("Cổ phiếu sàn Upcom"),
}

/** Khung thời gian nến. Giá trị trên API là [code], ví dụ "1H" */
@Serializable
enum class Resolution(val code: String, val description: String) {
    @SerialName("1")
    MINUTE_1("1", "1 phút"),
    @SerialName("3")
    MINUTE_3("3", "3 phút"),
    @SerialName("5")
    MINUTE_5("5", "5 phút"),
    @SerialName("15")
    MINUTE_15("15", "15 phút"),
    @SerialName("30")
    MINUTE_30("30", "30 phút"),
    @SerialName("1H")
    HOUR_1("1H", "1 giờ"),
    @SerialName("1D")
    DAY_1("1D", "1 ngày"),
    @SerialName("1W")
    WEEK_1("1W", "1 tuần"),
}

/** Định danh nhóm chứng khoán */
@Serializable
enum class SecurityGroupId(val description: String) {
    UNSPECIFIED("Không xác định"),
    BS("Trái phiếu doanh nghiệp"),
    EF("Quỹ ETF"),
    EW("Chứng quyền"),
    FU("Hợp đồng tương lai"),
    SR("Quyền mua"),
    ST("Cổ phiếu"),
}

/** Trạng thái của mã chứng khoán */
@Serializable
enum class SecurityStatus(val description: String) {
    HALT("Ngừng giao dịch"),
    NO_HALT("Không ngừng giao dịch"),
}

/** Chiều xác định mua/bán chủ động */
@Serializable
enum class Side(val description: String) {
    UNSPECIFIED("Không xác định"),
    BUY("Mua chủ động"),
    SELL("Bán chủ động"),
}

/** Trạng thái quản lý hành chính mã chứng khoán */
@Serializable
enum class SymbolAdminStatusCode(val description: String) {
    UNSPECIFIED("Không xác định"),
    CR("Kiểm soát và hạn chế giao dịch"),
    CTR("Kiểm soát"),
    NRM("Bình thường"),
    RES("Hạn chế giao dịch"),
    WFR("Cảnh báo do vi phạm BCTC"),
    WID("Cảnh báo do vi phạm CBTT"),
    WOV("Cảnh báo vi phạm khác"),
}

/** Trạng thái cơ chế giao dịch mã chứng khoán */
@Serializable
enum class SymbolTradingMethodStatusCode(val description: String) {
    UNSPECIFIED("Không xác định"),
    NRM("Bình thường"),
    NWE("Niêm yết mới biên độ đặc biệt"),
    NWN("Niêm yết mới biên độ thường"),
    SLS("Giao dịch đặc biệt sau halt"),
    SNE("Giao dịch đặc biệt"),
}

/** Tình trạng giao dịch của mã chứng khoán */
@Serializable
enum class SymbolTradingSanctionStatusCode(val description: String) {
    UNSPECIFIED("Không xác định"),
    NRM("Bình thường"),
    SUS("Tạm ngừng"),
    DTL("Hủy niêm yết chuyển sàn"),
    TFR("Ngưng giao dịch do hạn chế"),
}

/** Mã phiên giao dịch (theo từng mã). Giá trị trên API là [code], ví dụ "40" */
@Serializable
enum class TradingSessionId(val code: String, val description: String) {
    @SerialName("UNSPECIFIED")
    UNSPECIFIED("UNSPECIFIED", "Không xác định"),
    @SerialName("10")
    ATO("10", "ATO"),
    @SerialName("30")
    ATC("30", "ATC"),
    @SerialName("40")
    CONTINUOUS("40", "Phiên liên tục"),
    @SerialName("80")
    PCA_HALT("80", "PCA mã halt"),
    @SerialName("99")
    CLOSED("99", "Đóng cửa"),
}

/** Loại nhóm thị trường */
@Serializable
enum class MarketType(val description: String) {
    STOCK("Cổ phiếu"),
    DERIVATIVE("Phái sinh"),
    INDEX("Chỉ số"),
}

/** Phân loại mã hợp đồng phái sinh theo thời gian đáo hạn (áp dụng cho DERIVATIVE) */
@Serializable
enum class SymbolType(val description: String) {
    VN30F1M("HĐTL chỉ số VN30 1 tháng"),
    VN30F2M("HĐTL chỉ số VN30 2 tháng"),
    VN30F1Q("HĐTL chỉ số VN30 1 quý"),
    VN30F2Q("HĐTL chỉ số VN30 2 quý"),
}

/** Khung thời gian thống kê top cổ phiếu ảnh hưởng chỉ số. Giá trị trên API là [code], ví dụ "7" */
@Serializable
enum class InfluenceResolution(val code: String, val description: String) {
    @SerialName("1")
    DAY_1("1", "Trong ngày giao dịch hiện tại"),
    @SerialName("7")
    DAY_7("7", "Tích lũy 7 ngày"),
    @SerialName("14")
    DAY_14("14", "Tích lũy 14 ngày"),
    @SerialName("30")
    DAY_30("30", "Tích lũy 30 ngày"),
}
