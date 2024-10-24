package com.erunjrun.board.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.erunjrun.image.dto.ImageDTO;

public class RunBoardDTO {
	
	// 러닝코스 게시판 테이블
	private int board_idx;
	private String id; // 회원 테이블 fk
	private String nickname; // 회원테이블의 닉네임
	private String code_name;
	private String subject;
	private String content;
	private Date create_date;
	private Date update_date;
	private int bHit;
	private int likes;
	private String use_yn; // 활성여부
	private String is_map; // 지도 사용 여부
	
	// 지도 테이블
	private BigDecimal latitude;
	private BigDecimal longtude;
	private String path;
	private int order_num;
	
    // 지도 데이터 (경로 정보)
    private List<BigDecimal> latitudeList;
    private List<BigDecimal> longitudeList;
    private List<String> pathList;

    // 이미지 리스트
    private List<ImageDTO> imageList;
	
	
	public List<BigDecimal> getLatitudeList() {
		return latitudeList;
	}
	public void setLatitudeList(List<BigDecimal> latitudeList) {
		this.latitudeList = latitudeList;
	}
	public List<BigDecimal> getLongitudeList() {
		return longitudeList;
	}
	public void setLongitudeList(List<BigDecimal> longitudeList) {
		this.longitudeList = longitudeList;
	}
	public List<String> getPathList() {
		return pathList;
	}
	public void setPathList(List<String> pathList) {
		this.pathList = pathList;
	}
	public List<ImageDTO> getImageList() {
		return imageList;
	}
	public void setImageList(List<ImageDTO> imageList) {
		this.imageList = imageList;
	}
	public int getBoard_idx() {
		return board_idx;
	}
	public void setBoard_idx(int board_idx) {
		this.board_idx = board_idx;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode_name() {
		return code_name;
	}
	public void setCode_name(String code_name) {
		this.code_name = code_name;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public int getbHit() {
		return bHit;
	}
	public void setbHit(int bHit) {
		this.bHit = bHit;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getIs_map() {
		return is_map;
	}
	public void setIs_map(String is_map) {
		this.is_map = is_map;
	}
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	public BigDecimal getLongtude() {
		return longtude;
	}
	public void setLongtude(BigDecimal longtude) {
		this.longtude = longtude;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getOrder_num() {
		return order_num;
	}
	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	
	
	
	
}
