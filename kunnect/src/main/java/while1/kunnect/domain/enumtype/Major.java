package while1.kunnect.domain.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Major {
    // 융합기술대학
    MECHANICAL_ENGINEERING("기계공학과"),
    AUTOMOTIVE_ENGINEERING("자동차공학과"),
    AERONAUTICAL_MECHANICAL_DESIGN("항공·기계설계학과"),
    ELECTRICAL_ENGINEERING("전기공학과"),
    ELECTRONIC_ENGINEERING("전자공학과"),
    COMPUTER_ENGINEERING("컴퓨터공학과"),
    COMPUTER_SOFTWARE("컴퓨터소프트웨어학과"),
    AI_ROBOTICS_ENGINEERING("AI로봇공학과"),
    BIOMEDICAL_ENGINEERING("바이오메디컬융합학과"),
    PRECISION_MEDICINE_MEDICAL_DEVICE("정밀의료·의료기기학과"),

    // 공과대학
    CIVIL_ENGINEERING("사회기반공학전공"),
    ENVIRONMENTAL_ENGINEERING("환경공학전공"),
    URBAN_TRANSPORTATION_ENGINEERING("도시·교통공학전공"),
    CHEMICAL_BIOLOGICAL_ENGINEERING("화공생물공학과"),
    MATERIALS_SCIENCE_ENGINEERING("반도체신소재공학과"),
    POLYMER_SCIENCE_ENGINEERING("나노화학소재공학과"),
    INDUSTRIAL_MANAGEMENT_ENGINEERING("산업경영공학과"),
    SAFETY_ENGINEERING("안전공학과"),
    ARCHITECTURAL_ENGINEERING("건축공학과"),
    ARCHITECTURE("건축학과"),
    INDUSTRIAL_DESIGN("산업디자인학과"),
    COMMUNICATION_DESIGN("커뮤니케이션디자인학과"),

    // 인문대학
    ENGLISH_LANGUAGE_LITERATURE("영어영문학과"),
    CHINESE_LANGUAGE("중국어학과"),
    KOREAN_LANGUAGE_LITERATURE("한국어문학과"),
    MUSIC("음악학과"),
    SPORTS_MEDICINE("스포츠의학과"),
    SPORTS_INDUSTRY("스포츠산업학과"),

    // 사회과학대학
    PUBLIC_ADMINISTRATION("행정학과"),
    PUBLIC_ADMINISTRATION_INFO_CONVERGENCE("행정정보융합학과"),
    BUSINESS_ADMINISTRATION("경영학과"),
    CONVERGENCE_MANAGEMENT("융합경영학과"),
    INTERNATIONAL_TRADE_BUSINESS("국제무역학과"),
    SOCIAL_WELFARE("사회복지학과"),
    AIRLINE_SERVICE("항공서비스학과"),
    AERONAUTICAL_SCIENCE_FLIGHT_OPERATION("항공운항학과"),
    EARLY_CHILDHOOD_EDUCATION("유아교육학과"),
    MEDIA_CONTENTS("미디어&콘텐츠학과"),

    // 보건생명대학
    NURSING("간호학과"),
    PHYSICAL_THERAPY("물리치료학과"),
    PARAMEDICINE("응급구조학과"),
    FOOD_SCIENCE_TECHNOLOGY("식품공학전공"),
    FOOD_NUTRITION("식품영양학전공"),
    BIOTECHNOLOGY("생명공학전공"),
    EARLY_CHILDHOOD_SPECIAL_EDUCATION("유아특수교육학과"),
    IT_APPLIED_CONVERGENCE("IT응용융합학과"),
    INDUSTRIAL_COSMETIC_SCIENCE("화장품산업학과"),
    NATURAL_MATERIALS("천연물소재학과"),

    // 철도대학
    RAILROAD_MANAGEMENT_LOGISTICS("철도경영·물류학과"),
    DATA_SCIENCE("데이터사이언스전공"),
    ARTIFICIAL_INTELLIGENCE("인공지능전공"),
    RAILROAD_OPERATION_SYSTEMS_ENGINEERING("철도운전시스템공학과"),
    RAILWAY_VEHICLE_SYSTEM_ENGINEERING("철도차량시스템공학과"),
    RAILROAD_INFRASTRUCTURE_ENGINEERING("철도인프라공학과"),
    RAILROAD_ELECTRICAL_INFO_ENGINEERING("철도전기정보공학과"),

    // 미래융합대학
    SAFETY_CONVERGENCE_ENGINEERING("안전융합공학과"),
    CONSTRUCTION_DISASTER_PREVENTION("건설방재융합공학과"),
    SPORT_WELFARE("스포츠복지학과"),
    WELFARE_MANAGEMENT("복지·경영학과"),
    SMART_RAILWAY_TRANSPORTATION("스마트철도교통공학과"),
    SECONDARY_BATTERY_ENGINEERING("이차전지공학과"),

    // 교양학부/자유전공학부/창의융합학부
    LIBERAL_ARTS("교양학부"),
    LIBERAL_STUDIES("자유전공학부"),
    CREATIVE_CONVERGENCE("창의융합학부");

    private final String info;
}
