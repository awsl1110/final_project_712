import{d as $,r as k,n as I,o,c as d,b as m,w as i,e as t,I as L,q as y,G as w,H as C,g as P,E as r,h as p,J as S,t as n,f,L as h,u as x,R as q,P as R,Q as B,_ as j}from"./index-BekgHCmB.js";import{g as F,a as G,d as H}from"./order-D64j-J59.js";import"./user-DuJHeInz.js";const J={class:"orders-container"},Q={class:"orders-content"},Y={class:"order-header"},K={class:"order-info"},U={class:"order-number"},W={class:"order-time"},X={class:"order-status"},Z={class:"order-items"},ee={class:"item-info"},te={class:"item-details"},se={class:"item-name"},ae={class:"item-price"},oe={class:"subtotal"},ne={class:"order-footer"},ce={class:"order-total"},re={class:"amount-detail"},ie={key:0},le={class:"final-amount"},de={class:"order-actions"},ue=$({__name:"orders",setup(_e){const b=P(),u=k([]),g=k(!0),N=async()=>{try{g.value=!0;const e=await F();e.data.code===200?u.value=e.data.data:e.data.code===500?r.error("服务器内部错误"):r.error(e.data.message||"获取订单列表失败")}catch(e){console.error("获取订单列表失败:",e),r.error("网络错误，请稍后重试")}finally{g.value=!1}},T=async e=>{try{await B.confirm("确定要取消这个订单吗？","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"});const l=(await G(e.id)).data;l.code===200?(e.status=4,r.success("订单已取消")):r.error(l.message||"取消订单失败")}catch(s){s!=="cancel"&&r.error("取消订单失败")}},z=async e=>{try{await B.confirm("确定要删除这个订单吗？","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"});const s=await H(e.id);s.code===200?(u.value=u.value.filter(l=>l.id!==e.id),r.success("删除成功")):r.error(s.message||"删除失败")}catch(s){s!=="cancel"&&r.error("删除失败")}},D=e=>({0:"待付款",1:"待发货",2:"已发货",3:"已完成",4:"已取消"})[e]||"未知状态",_=e=>e==null?"¥0.00":e.toLocaleString("zh-CN",{style:"currency",currency:"CNY"}),M=e=>{b.push(`/order/${e}`)};return I(()=>{N()}),(e,s)=>{const l=p("el-card"),V=p("el-empty"),A=p("el-tag"),E=p("el-image"),v=p("el-button"),O=S("loading");return o(),d("div",J,[m(l,{class:"page-header"},{header:i(()=>s[0]||(s[0]=[t("div",{class:"header"},[t("span",{class:"title"},"我的订单")],-1)])),_:1}),L((o(),d("div",Q,[u.value.length===0?(o(),y(V,{key:0,description:"暂无订单"})):(o(!0),d(w,{key:1},C(u.value,a=>(o(),y(l,{key:a.id,class:"order-card"},{header:i(()=>[t("div",Y,[t("div",K,[t("span",U,"订单号："+n(a.id),1),t("span",W,"下单时间："+n(a.createTime),1)]),t("div",X,[m(A,{type:a.status===4?"info":a.status===0?"warning":"success"},{default:i(()=>[f(n(D(a.status)),1)]),_:2},1032,["type"])])])]),default:i(()=>[t("div",Z,[(o(!0),d(w,null,C(a.items,c=>(o(),d("div",{key:c.id,class:"order-item"},[t("div",ee,[m(E,{src:c.productImage||"/default-product.jpg",fit:"cover",class:"item-image"},null,8,["src"]),t("div",te,[t("h3",se,n(c.productName),1),t("div",ae,[t("span",null,n(_(c.productPrice))+" × "+n(c.quantity),1),t("span",oe,"小计："+n(_(c.subtotal)),1)])])])]))),128))]),t("div",ne,[t("div",ce,[t("div",re,[t("p",null,"商品总额："+n(_(a.totalAmount)),1),a.discountAmount>0?(o(),d("p",ie,"优惠金额：-"+n(_(a.discountAmount)),1)):h("",!0),t("p",le,"实付金额："+n(_(a.payAmount)),1)])]),t("div",de,[a.status===0?(o(),y(v,{key:0,type:"primary",size:"small"},{default:i(()=>s[1]||(s[1]=[f(" 去付款 ")])),_:1})):h("",!0),a.status!==4?(o(),y(v,{key:1,type:"warning",size:"small",onClick:c=>T(a)},{default:i(()=>s[2]||(s[2]=[f(" 取消订单 ")])),_:2},1032,["onClick"])):h("",!0),m(v,{type:"info",size:"small",icon:x(q),onClick:c=>M(a.id)},{default:i(()=>s[3]||(s[3]=[f(" 订单详情 ")])),_:2},1032,["icon","onClick"]),m(v,{type:"danger",size:"small",icon:x(R),onClick:c=>z(a)},{default:i(()=>s[4]||(s[4]=[f(" 删除订单 ")])),_:2},1032,["icon","onClick"])])])]),_:2},1024))),128))])),[[O,g.value]])])}}}),ve=j(ue,[["__scopeId","data-v-60639b34"]]);export{ve as default};