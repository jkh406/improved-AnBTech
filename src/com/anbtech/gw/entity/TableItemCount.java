package com.anbtech.gw.entity;
public class TableItemCount
{
	//전자 결재 수량
	private int app_ing_cnt;		//미결함 수량
	private int ask_ing_cnt;		//진행함 수량

	//변동시 추가항목
	private int app_box_cnt;		//기결함 수량 (총수량)
	private int app_gen_cnt;		//기결함 수량 (일반문서)
	private int app_ser_cnt;		//기결함 수량 (고객관리)

	private int app_out_cnt;		//기결함 수량 (외출계 : an outing)
	private int app_btr_cnt;		//기결함 수량 (출장신청 : a business trip)
	private int app_hdy_cnt;		//기결함 수량 (휴가원 : holidays)
	private int app_car_cnt;		//기결함 수량 (배차신청 : Allocation of Cars)
	private int app_rep_cnt;		//기결함 수량 (보고서 : Report)
	private int app_brp_cnt;		//기결함 수량 (출장보고서 : a business trip report)
	private int app_drf_cnt;		//기결함 수량 (기안서 : drafting)
	private int app_crd_cnt;		//기결함 수량 (명함신청성 : a business card)
	private int app_rsn_cnt;		//기결함 수량 (사유서 : a reson)
	private int app_hlp_cnt;		//기결함 수량 (협조전 : help)
	private int app_otw_cnt;		//기결함 수량 (연장근무신청서 : overtime work)
	private int app_off_cnt;		//기결함 수량 (구인의뢰서 : job offering)
	private int app_edu_cnt;		//기결함 수량 (교육일지 : education)
	private int app_akg_cnt;		//기결함 수량 (승인원 : acknowledgment)
	private int app_td_cnt;			//기결함 수량 (기술문서 : technical document)
	private int app_odt_cnt;		//기결함 수량 (공지공문 : an official document)
	private int app_ids_cnt;		//기결함 수량 (사내공문 : an internal official document)
	private int app_ods_cnt;		//기결함 수량 (사외공문 : an outside official document)
	private int app_ast_cnt;		//기결함 수량 (자산관리 : an asset)
	private int app_est_cnt;		//기결함 수량 (견적서 : estimate)
	private int app_ewk_cnt;		//기결함 수량 (특근신청 : extra work)
	private int app_bom_cnt;		//기결함 수량 (BOM구성 : bill of material)
	private int app_dcm_cnt;		//기결함 수량 (설게변경 : design change management)
	private int app_pcr_cnt;		//기결함 수량 (구매요청 : purchase request)
	private int app_odr_cnt;		//기결함 수량 (발주요청 : order request)
	private int app_pwh_cnt;		//기결함 수량 (구매입고 : purchase warehousing)
	private int app_tgw_cnt;		//기결함 수량 (부품출고 : take goods out of a warehouse)
	
	//공통
	private int rej_box_cnt;		//반려함 수량
	private int tmp_box_cnt;		//임시저장함 수량
	private int see_box_cnt;		//통보함 수량 (읽은수량)
	private int see_box_tot;		//통보함 수량 (전체수량)

	private int del_box_cnt;		//삭제함 수량

	//개인우편 수량
	private int post_cnt;			//도착 편지 수량

	//공지사항 수량
	private int inform_cnt;			//공지사항 수량
	

	/**************************************************************
	//	help method 정의
	**************************************************************/
	//미결함 수량
	public void setAppIngCnt(int app_ing_cnt)	{	this.app_ing_cnt = app_ing_cnt;	}
	public int getAppIngCnt()					{	return this.app_ing_cnt;		}

	//진행함 수량
	public void setAskIngCnt(int ask_ing_cnt)	{	this.ask_ing_cnt = ask_ing_cnt;	}
	public int getAskIngCnt()					{	return this.ask_ing_cnt;		}


	//기결함 총수량
	public void setAppBoxCnt(int app_box_cnt)	{	this.app_box_cnt = app_box_cnt;	}
	public int getAppBoxCnt()					{	return this.app_box_cnt;		}

	//기결함 일반문서
	public void setAppGenCnt(int app_gen_cnt)	{	this.app_gen_cnt = app_gen_cnt;	}
	public int getAppGenCnt()					{	return this.app_gen_cnt;		}

	//기결함 고객관리 문서
	public void setAppSerCnt(int app_ser_cnt)	{	this.app_ser_cnt = app_ser_cnt;	}
	public int getAppSerCnt()					{	return this.app_ser_cnt;		}

	//기결함 (외출계 : an outing) 문서
	public void setAppOutCnt(int app_out_cnt)	{	this.app_out_cnt = app_out_cnt;	}
	public int getAppOutCnt()					{	return this.app_out_cnt;		}

	//기결함 (출장신청 : a business trip) 문서
	public void setAppBtrCnt(int app_btr_cnt)	{	this.app_btr_cnt = app_btr_cnt;	}
	public int getAppBtrCnt()					{	return this.app_btr_cnt;		}

	//기결함 (휴가원 : holidays) 문서
	public void setAppHdyCnt(int app_hdy_cnt)	{	this.app_hdy_cnt = app_hdy_cnt;	}
	public int getAppHdyCnt()					{	return this.app_hdy_cnt;		}

	//기결함 (배차신청 : Allocation of cars) 문서
	public void setAppCarCnt(int app_car_cnt)	{	this.app_car_cnt = app_car_cnt;	}
	public int getAppCarCnt()					{	return this.app_car_cnt;		}

	//기결함 (보고서 : Report) 문서
	public void setAppRepCnt(int app_rep_cnt)	{	this.app_rep_cnt = app_rep_cnt;	}
	public int getAppRepCnt()					{	return this.app_rep_cnt;		}
	
	//기결함 (출장보고서 : a business trip Report) 문서
	public void setAppBrpCnt(int app_brp_cnt)	{	this.app_brp_cnt = app_brp_cnt;	}
	public int getAppBrpCnt()					{	return this.app_brp_cnt;		}
	
	//기결함 (기안서 : Drafting) 문서
	public void setAppDrfCnt(int app_drf_cnt)	{	this.app_drf_cnt = app_drf_cnt;	}
	public int getAppDrfCnt()					{	return this.app_drf_cnt;		}
	
	//기결함 (명함신청서 : a business card) 문서
	public void setAppCrdCnt(int app_crd_cnt)	{	this.app_crd_cnt = app_crd_cnt;	}
	public int getAppCrdCnt()					{	return this.app_crd_cnt;		}
	
	//기결함 (사유서 : a reson) 문서
	public void setAppRsnCnt(int app_rsn_cnt)	{	this.app_rsn_cnt = app_rsn_cnt;	}
	public int getAppRsnCnt()					{	return this.app_rsn_cnt;		}
	
	//기결함 (협조전 : help) 문서
	public void setAppHlpCnt(int app_hlp_cnt)	{	this.app_hlp_cnt = app_hlp_cnt;	}
	public int getAppHlpCnt()					{	return this.app_hlp_cnt;		}
	
	//기결함 (연장근무신청서 : overtime work) 문서
	public void setAppOtwCnt(int app_otw_cnt)	{	this.app_otw_cnt = app_otw_cnt;	}
	public int getAppOtwCnt()					{	return this.app_otw_cnt;		}
	
	//기결함 (구인의뢰서 : job offering) 문서
	public void setAppOffCnt(int app_off_cnt)	{	this.app_off_cnt = app_off_cnt;	}
	public int getAppOffCnt()					{	return this.app_off_cnt;		}
	
	//기결함 (교육일지 : Education) 문서
	public void setAppEduCnt(int app_edu_cnt)	{	this.app_edu_cnt = app_edu_cnt;	}
	public int getAppEduCnt()					{	return this.app_edu_cnt;		}

	//기결함 (승인원 : acknowledgment) 문서
	public void setAppAkgCnt(int app_akg_cnt)	{	this.app_akg_cnt = app_akg_cnt;	}
	public int getAppAkgCnt()					{	return this.app_akg_cnt;		}

	//기결함 (승인원 : acknowledgment) 문서
	public void setAppTdCnt(int app_td_cnt)		{	this.app_td_cnt = app_td_cnt;	}
	public int getAppTdCnt()					{	return this.app_td_cnt;			}

	//기결함 수량 (공지공문 : an official document)
	public void setAppOdtCnt(int app_odt_cnt)	{	this.app_odt_cnt = app_odt_cnt;	}
	public int getAppOdtCnt()					{	return this.app_odt_cnt;		}

	//기결함 수량 (사내공문 : an internal official document)
	public void setAppIdsCnt(int app_ids_cnt)	{	this.app_ids_cnt = app_ids_cnt;	}
	public int getAppIdsCnt()					{	return this.app_ids_cnt;		}

	//기결함 수량 (사외공문 : an outside official document)
	public void setAppOdsCnt(int app_ods_cnt)	{	this.app_ods_cnt = app_ods_cnt;	}
	public int getAppOdsCnt()					{	return this.app_ods_cnt;		}

	//기결함 수량 (자산관리 : an asset)
	public void setAppAstCnt(int app_ast_cnt)	{	this.app_ast_cnt = app_ast_cnt;	}
	public int getAppAstCnt()					{	return this.app_ast_cnt;		}

	//기결함 수량 (견적서 : estimate)
	public void setAppEstCnt(int app_est_cnt)	{	this.app_est_cnt = app_est_cnt;	}
	public int getAppEstCnt()					{	return this.app_est_cnt;		}

	//기결함 수량 (특근신청 : extra work)
	public void setAppEwkCnt(int app_ewk_cnt)	{	this.app_ewk_cnt = app_ewk_cnt;	}
	public int getAppEwkCnt()					{	return this.app_ewk_cnt;		}

	//기결함 수량 (BOM구성 : bill of material)
	public void setAppBomCnt(int app_bom_cnt)	{	this.app_bom_cnt = app_bom_cnt;	}
	public int getAppBomCnt()					{	return this.app_bom_cnt;		}

	//기결함 수량 (설게변경 : design change management)
	public void setAppDcmCnt(int app_dcm_cnt)	{	this.app_dcm_cnt = app_dcm_cnt;	}
	public int getAppDcmCnt()					{	return this.app_dcm_cnt;		}

	//기결함 수량 (구매요청 : purchase request)
	public void setAppPcrCnt(int app_pcr_cnt)	{	this.app_pcr_cnt = app_pcr_cnt;	}
	public int getAppPcrCnt()					{	return this.app_pcr_cnt;		}

	//기결함 수량 (발주요청 : order request)
	public void setAppOdrCnt(int app_odr_cnt)	{	this.app_odr_cnt = app_odr_cnt;	}
	public int getAppOdrCnt()					{	return this.app_odr_cnt;		}

	//기결함 수량 (구매입고 : purchase warehousing)
	public void setAppPwhCnt(int app_pwh_cnt)	{	this.app_pwh_cnt = app_pwh_cnt;	}
	public int getAppPwhCnt()					{	return this.app_pwh_cnt;		}

	//기결함 수량 (부품출고 : take goods out of a warehouse)
	public void setAppTgwCnt(int app_tgw_cnt)	{	this.app_tgw_cnt = app_tgw_cnt;	}
	public int getAppTgwCnt()					{	return this.app_tgw_cnt;		}

	//반려함 수량
	public void setRejBoxCnt(int rej_box_cnt)	{	this.rej_box_cnt = rej_box_cnt;	}
	public int getRejBoxCnt()					{	return this.rej_box_cnt;		}

	//저장함 수량
	public void setTmpBoxCnt(int tmp_box_cnt)	{	this.tmp_box_cnt = tmp_box_cnt;	}
	public int getTmpBoxCnt()					{	return this.tmp_box_cnt;		}

	//통보함 (읽은) 수량
	public void setSeeBoxCnt(int see_box_cnt)	{	this.see_box_cnt = see_box_cnt;	}
	public int getSeeBoxCnt()					{	return this.see_box_cnt;		}
	
	//통보함 (전체) 수량
	public void setSeeBoxTot(int see_box_tot)	{	this.see_box_tot = see_box_tot;	}
	public int getSeeBoxTot()					{	return this.see_box_tot;		}
	
	//삭제함 수량
	public void setDelBoxCnt(int del_box_cnt)	{	this.del_box_cnt = del_box_cnt;	}
	public int getDelBoxCnt()					{	return this.del_box_cnt;		}
	
	//개인우편함 수량
	public void setPostCnt(int post_cnt)		{	this.post_cnt = post_cnt;		}
	public int getPostCnt()						{	return this.post_cnt;			}
	
	//공지사항 수량
	public void setInformCnt(int inform_cnt)	{	this.inform_cnt = inform_cnt;	}
	public int getInformCnt()					{	return this.inform_cnt;			}

	//------------------------END------------------------------------//
}
