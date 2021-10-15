	let pet = undefined;
	
	const printBoard = ()=>{
		console.log(page);
		const $list = $("#list");
		// 서버가 보낸 board들은 page.content에 저장되어 있다
		for(c of page.content) {
			let createdTime = c.writeTime.substr(-26,10);
			const $tr = $("<tr>").appendTo($list);
			$("<td>").text(c.bno).appendTo($tr);
			const $td = $("<td>").appendTo($tr);
			if(c.attachmentCnt>0)
				$("<i class='fa fa-paperclip'></i>").appendTo($td);
			$("<a>").attr("href", "/board/read?bno=" + c.bno).text(c.title).appendTo($td);
			if(c.commentCnt>0)
				$("<span>").text(" [" + c.commentCnt + "]").appendTo($td);
			$("<td>").text(c.writer).appendTo($tr);
			$("<td>").text(createdTime).appendTo($tr);
			$("<td>").text(c.readCnt).appendTo($tr);
		}
	};
	
	const getPagination = () => {
		// 한번에 다섯개의 페이지씩
		const blockSize = 5;
		
		// 서버 응답에 현재 페이지가 포함되어 있지 않다....재계산하자
		let pageno = location.search.substr(8);
		if(pageno=="")
			pageno=1;
		
		// 0번 블록 : 1~5 page, 1번 블록 : 6~10 page
		const blockNo = Math.floor((pageno-1)/blockSize);
		const prev = blockNo * blockSize;
		const first = prev + 1;
		let last = first + blockSize - 1;
		let next = last + 1;
		const countOfPage = Math.ceil(page.totalcount/10)
		if(last>=countOfPage) {
			last = countOfPage;
			next = 0;
		}
		return {pageno, prev, next, first, last};
		
	};
	
	// 구조 분해 할당 : 객체를 변수로 풀어헤치는 문법
	// const {pageno, prev, next, first, last} = getPagination();
	const printPagination = ({pageno, prev, next, first, last}) => {
		const $pagination = $("ul.pagination");
		const url = "/board/list?pageno="
				
		// 이전으로 
		if(prev>0) {
			const $li = $("<li>").appendTo($pagination)
			$("<a>").attr("href", url+prev).text("이전으로").appendTo($li);
		}
		
		// 시작 페이지에서 마지막 페이지....현재 페이지 번호일 경우 active 클래스 추가
		for(let idx=first; idx<=last; idx++) {
			const $li = $("<li>").appendTo($pagination)
			$("<a>").attr("href", url+idx).text(idx).appendTo($li);
			if(idx==pageno)
				$li.attr("class", "active");
		}
		
		// 다음으로
		if(next>0) {
			const $li = $("<li>").appendTo($pagination)
			$("<a>").attr("href", url+next).text("다음으로").appendTo($li);
		}
	}
	
	
	$(document).ready(()=>{
	
	let pageno = location.search.substr(8);
		if(pageno=="")
			pageno=1;
		
		$.ajax("/board/all?pageno="+pageno).done(result=>{
			page=result;
			printBoard();
			printPagination(getPagination());
		});
	
		
		//내 정보 보기
		$("#my_info").on("click", ()=>{
			location.href ="/member/read"
		});
		
		//내 펫 정보 보기
		$("#my_pet").on("click", ()=>{
			location.href ="/member/pet"
		});
		
		$("#addPet").on("click", ()=>{
			let url = '/member/pet_add';
  			let name = 'petOpen';
  			let option = "width=520, height=700, left=500, top=250";
  			window.open(url, name, option);
		});
		
	})