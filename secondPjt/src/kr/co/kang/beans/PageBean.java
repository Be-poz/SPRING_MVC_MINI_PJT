package kr.co.kang.beans;

public class PageBean {
	//�ּ� ������ ��ȣ
	private int min;
	//�ִ� ������ ��ȣ
	private int max;
	//���� ��ư�� ������ ��ȣ
	private int prevPage;
	//���� ��ư�� ������ ��ȣ
	private int nextPage;
	//��ü ������ ����
	private int pageCnt;
	//���� ������ ����
	private int currentPage;
	
	//contentCnt:��ü �� ����, currentPage: ���� �� ��ȣ, contentPageCnt: ������ �� ���� ����, paginationCnt: ������ ��ư�� ����
	public PageBean(int contentCnt, int currentPage,int contentPageCnt, int paginationCnt) {
		this.currentPage=currentPage;
		pageCnt=contentCnt/contentPageCnt;
		if(contentCnt%contentPageCnt>0) pageCnt++;
		min=((currentPage-1)/paginationCnt)*paginationCnt+1;
		max=min+paginationCnt-1;
		if(max>pageCnt) max=pageCnt;
		prevPage=min-1;
		nextPage=max+1;
		if(nextPage>pageCnt) nextPage=pageCnt;
		
	}
	
	public int getMin() {
		return min;
	}
	public int getMax() {
		return max;
	}
	public int getPrevPage() {
		return prevPage;
	}
	public int getNextPage() {
		return nextPage;
	}
	public int getPageCnt() {
		return pageCnt;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	
}
