package com.gachon.footprint.data

data class ModelFoot (
    //작성 유저 Id,메세지 Id
    var uId : String? =null,
    var footid : String? = null,
    //닉네임과 텍스트메세지,미디어메세지 경로
    var nickname : String? = null,
    var msgText : String? = null,
    var msgImageUrl : String? = null,
    var msgMediaUrl : String? = null,
    //작성 시간과 defaultSetTieme에서 시간을 뺀 나머지 시간 초로 표시
    var writtenTime : Long? = null,
    var remainTime : Int? = null,
    //AR마커,맵 마커 리스트(Cloud Storage)
    var arMarkerUrl : String? =null,
    var mapMarkerUrl : String? = null,
    // 현재좌표 받을 곳
    var msgLocation : String? = null,
    // 좋아요 수 count
    var recommendCount : Int? = null,
    var reportCount : Int? =null)
    //footMsg 내 컬렉션 Review 생성
    {
        data class Review(
            var uid: String? = null,
            var nickname: String? = null,
            var msgText: String? = null,
            var writtenTime : Long? = null,
            //추천과 신고
            var recommend : Boolean? = null,
            var reportIt : Boolean? = null)
    }
