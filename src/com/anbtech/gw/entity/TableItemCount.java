package com.anbtech.gw.entity;
public class TableItemCount
{
	//���� ���� ����
	private int app_ing_cnt;		//�̰��� ����
	private int ask_ing_cnt;		//������ ����

	//������ �߰��׸�
	private int app_box_cnt;		//����� ���� (�Ѽ���)
	private int app_gen_cnt;		//����� ���� (�Ϲݹ���)
	private int app_ser_cnt;		//����� ���� (������)

	private int app_out_cnt;		//����� ���� (����� : an outing)
	private int app_btr_cnt;		//����� ���� (�����û : a business trip)
	private int app_hdy_cnt;		//����� ���� (�ް��� : holidays)
	private int app_car_cnt;		//����� ���� (������û : Allocation of Cars)
	private int app_rep_cnt;		//����� ���� (���� : Report)
	private int app_brp_cnt;		//����� ���� (���庸�� : a business trip report)
	private int app_drf_cnt;		//����� ���� (��ȼ� : drafting)
	private int app_crd_cnt;		//����� ���� (���Խ�û�� : a business card)
	private int app_rsn_cnt;		//����� ���� (������ : a reson)
	private int app_hlp_cnt;		//����� ���� (������ : help)
	private int app_otw_cnt;		//����� ���� (����ٹ���û�� : overtime work)
	private int app_off_cnt;		//����� ���� (�����Ƿڼ� : job offering)
	private int app_edu_cnt;		//����� ���� (�������� : education)
	private int app_akg_cnt;		//����� ���� (���ο� : acknowledgment)
	private int app_td_cnt;			//����� ���� (������� : technical document)
	private int app_odt_cnt;		//����� ���� (�������� : an official document)
	private int app_ids_cnt;		//����� ���� (�系���� : an internal official document)
	private int app_ods_cnt;		//����� ���� (��ܰ��� : an outside official document)
	private int app_ast_cnt;		//����� ���� (�ڻ���� : an asset)
	private int app_est_cnt;		//����� ���� (������ : estimate)
	private int app_ewk_cnt;		//����� ���� (Ư�ٽ�û : extra work)
	private int app_bom_cnt;		//����� ���� (BOM���� : bill of material)
	private int app_dcm_cnt;		//����� ���� (���Ժ��� : design change management)
	private int app_pcr_cnt;		//����� ���� (���ſ�û : purchase request)
	private int app_odr_cnt;		//����� ���� (���ֿ�û : order request)
	private int app_pwh_cnt;		//����� ���� (�����԰� : purchase warehousing)
	private int app_tgw_cnt;		//����� ���� (��ǰ��� : take goods out of a warehouse)
	
	//����
	private int rej_box_cnt;		//�ݷ��� ����
	private int tmp_box_cnt;		//�ӽ������� ����
	private int see_box_cnt;		//�뺸�� ���� (��������)
	private int see_box_tot;		//�뺸�� ���� (��ü����)

	private int del_box_cnt;		//������ ����

	//���ο��� ����
	private int post_cnt;			//���� ���� ����

	//�������� ����
	private int inform_cnt;			//�������� ����
	

	/**************************************************************
	//	help method ����
	**************************************************************/
	//�̰��� ����
	public void setAppIngCnt(int app_ing_cnt)	{	this.app_ing_cnt = app_ing_cnt;	}
	public int getAppIngCnt()					{	return this.app_ing_cnt;		}

	//������ ����
	public void setAskIngCnt(int ask_ing_cnt)	{	this.ask_ing_cnt = ask_ing_cnt;	}
	public int getAskIngCnt()					{	return this.ask_ing_cnt;		}


	//����� �Ѽ���
	public void setAppBoxCnt(int app_box_cnt)	{	this.app_box_cnt = app_box_cnt;	}
	public int getAppBoxCnt()					{	return this.app_box_cnt;		}

	//����� �Ϲݹ���
	public void setAppGenCnt(int app_gen_cnt)	{	this.app_gen_cnt = app_gen_cnt;	}
	public int getAppGenCnt()					{	return this.app_gen_cnt;		}

	//����� ������ ����
	public void setAppSerCnt(int app_ser_cnt)	{	this.app_ser_cnt = app_ser_cnt;	}
	public int getAppSerCnt()					{	return this.app_ser_cnt;		}

	//����� (����� : an outing) ����
	public void setAppOutCnt(int app_out_cnt)	{	this.app_out_cnt = app_out_cnt;	}
	public int getAppOutCnt()					{	return this.app_out_cnt;		}

	//����� (�����û : a business trip) ����
	public void setAppBtrCnt(int app_btr_cnt)	{	this.app_btr_cnt = app_btr_cnt;	}
	public int getAppBtrCnt()					{	return this.app_btr_cnt;		}

	//����� (�ް��� : holidays) ����
	public void setAppHdyCnt(int app_hdy_cnt)	{	this.app_hdy_cnt = app_hdy_cnt;	}
	public int getAppHdyCnt()					{	return this.app_hdy_cnt;		}

	//����� (������û : Allocation of cars) ����
	public void setAppCarCnt(int app_car_cnt)	{	this.app_car_cnt = app_car_cnt;	}
	public int getAppCarCnt()					{	return this.app_car_cnt;		}

	//����� (���� : Report) ����
	public void setAppRepCnt(int app_rep_cnt)	{	this.app_rep_cnt = app_rep_cnt;	}
	public int getAppRepCnt()					{	return this.app_rep_cnt;		}
	
	//����� (���庸�� : a business trip Report) ����
	public void setAppBrpCnt(int app_brp_cnt)	{	this.app_brp_cnt = app_brp_cnt;	}
	public int getAppBrpCnt()					{	return this.app_brp_cnt;		}
	
	//����� (��ȼ� : Drafting) ����
	public void setAppDrfCnt(int app_drf_cnt)	{	this.app_drf_cnt = app_drf_cnt;	}
	public int getAppDrfCnt()					{	return this.app_drf_cnt;		}
	
	//����� (���Խ�û�� : a business card) ����
	public void setAppCrdCnt(int app_crd_cnt)	{	this.app_crd_cnt = app_crd_cnt;	}
	public int getAppCrdCnt()					{	return this.app_crd_cnt;		}
	
	//����� (������ : a reson) ����
	public void setAppRsnCnt(int app_rsn_cnt)	{	this.app_rsn_cnt = app_rsn_cnt;	}
	public int getAppRsnCnt()					{	return this.app_rsn_cnt;		}
	
	//����� (������ : help) ����
	public void setAppHlpCnt(int app_hlp_cnt)	{	this.app_hlp_cnt = app_hlp_cnt;	}
	public int getAppHlpCnt()					{	return this.app_hlp_cnt;		}
	
	//����� (����ٹ���û�� : overtime work) ����
	public void setAppOtwCnt(int app_otw_cnt)	{	this.app_otw_cnt = app_otw_cnt;	}
	public int getAppOtwCnt()					{	return this.app_otw_cnt;		}
	
	//����� (�����Ƿڼ� : job offering) ����
	public void setAppOffCnt(int app_off_cnt)	{	this.app_off_cnt = app_off_cnt;	}
	public int getAppOffCnt()					{	return this.app_off_cnt;		}
	
	//����� (�������� : Education) ����
	public void setAppEduCnt(int app_edu_cnt)	{	this.app_edu_cnt = app_edu_cnt;	}
	public int getAppEduCnt()					{	return this.app_edu_cnt;		}

	//����� (���ο� : acknowledgment) ����
	public void setAppAkgCnt(int app_akg_cnt)	{	this.app_akg_cnt = app_akg_cnt;	}
	public int getAppAkgCnt()					{	return this.app_akg_cnt;		}

	//����� (���ο� : acknowledgment) ����
	public void setAppTdCnt(int app_td_cnt)		{	this.app_td_cnt = app_td_cnt;	}
	public int getAppTdCnt()					{	return this.app_td_cnt;			}

	//����� ���� (�������� : an official document)
	public void setAppOdtCnt(int app_odt_cnt)	{	this.app_odt_cnt = app_odt_cnt;	}
	public int getAppOdtCnt()					{	return this.app_odt_cnt;		}

	//����� ���� (�系���� : an internal official document)
	public void setAppIdsCnt(int app_ids_cnt)	{	this.app_ids_cnt = app_ids_cnt;	}
	public int getAppIdsCnt()					{	return this.app_ids_cnt;		}

	//����� ���� (��ܰ��� : an outside official document)
	public void setAppOdsCnt(int app_ods_cnt)	{	this.app_ods_cnt = app_ods_cnt;	}
	public int getAppOdsCnt()					{	return this.app_ods_cnt;		}

	//����� ���� (�ڻ���� : an asset)
	public void setAppAstCnt(int app_ast_cnt)	{	this.app_ast_cnt = app_ast_cnt;	}
	public int getAppAstCnt()					{	return this.app_ast_cnt;		}

	//����� ���� (������ : estimate)
	public void setAppEstCnt(int app_est_cnt)	{	this.app_est_cnt = app_est_cnt;	}
	public int getAppEstCnt()					{	return this.app_est_cnt;		}

	//����� ���� (Ư�ٽ�û : extra work)
	public void setAppEwkCnt(int app_ewk_cnt)	{	this.app_ewk_cnt = app_ewk_cnt;	}
	public int getAppEwkCnt()					{	return this.app_ewk_cnt;		}

	//����� ���� (BOM���� : bill of material)
	public void setAppBomCnt(int app_bom_cnt)	{	this.app_bom_cnt = app_bom_cnt;	}
	public int getAppBomCnt()					{	return this.app_bom_cnt;		}

	//����� ���� (���Ժ��� : design change management)
	public void setAppDcmCnt(int app_dcm_cnt)	{	this.app_dcm_cnt = app_dcm_cnt;	}
	public int getAppDcmCnt()					{	return this.app_dcm_cnt;		}

	//����� ���� (���ſ�û : purchase request)
	public void setAppPcrCnt(int app_pcr_cnt)	{	this.app_pcr_cnt = app_pcr_cnt;	}
	public int getAppPcrCnt()					{	return this.app_pcr_cnt;		}

	//����� ���� (���ֿ�û : order request)
	public void setAppOdrCnt(int app_odr_cnt)	{	this.app_odr_cnt = app_odr_cnt;	}
	public int getAppOdrCnt()					{	return this.app_odr_cnt;		}

	//����� ���� (�����԰� : purchase warehousing)
	public void setAppPwhCnt(int app_pwh_cnt)	{	this.app_pwh_cnt = app_pwh_cnt;	}
	public int getAppPwhCnt()					{	return this.app_pwh_cnt;		}

	//����� ���� (��ǰ��� : take goods out of a warehouse)
	public void setAppTgwCnt(int app_tgw_cnt)	{	this.app_tgw_cnt = app_tgw_cnt;	}
	public int getAppTgwCnt()					{	return this.app_tgw_cnt;		}

	//�ݷ��� ����
	public void setRejBoxCnt(int rej_box_cnt)	{	this.rej_box_cnt = rej_box_cnt;	}
	public int getRejBoxCnt()					{	return this.rej_box_cnt;		}

	//������ ����
	public void setTmpBoxCnt(int tmp_box_cnt)	{	this.tmp_box_cnt = tmp_box_cnt;	}
	public int getTmpBoxCnt()					{	return this.tmp_box_cnt;		}

	//�뺸�� (����) ����
	public void setSeeBoxCnt(int see_box_cnt)	{	this.see_box_cnt = see_box_cnt;	}
	public int getSeeBoxCnt()					{	return this.see_box_cnt;		}
	
	//�뺸�� (��ü) ����
	public void setSeeBoxTot(int see_box_tot)	{	this.see_box_tot = see_box_tot;	}
	public int getSeeBoxTot()					{	return this.see_box_tot;		}
	
	//������ ����
	public void setDelBoxCnt(int del_box_cnt)	{	this.del_box_cnt = del_box_cnt;	}
	public int getDelBoxCnt()					{	return this.del_box_cnt;		}
	
	//���ο����� ����
	public void setPostCnt(int post_cnt)		{	this.post_cnt = post_cnt;		}
	public int getPostCnt()						{	return this.post_cnt;			}
	
	//�������� ����
	public void setInformCnt(int inform_cnt)	{	this.inform_cnt = inform_cnt;	}
	public int getInformCnt()					{	return this.inform_cnt;			}

	//------------------------END------------------------------------//
}
