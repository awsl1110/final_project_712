import{d as V,r as h,n as S,o as _,c as w,e as t,t as p,q as m,w as a,b as c,f as C,u as f,H as q,F as z,G as L,g as R,E as r,h as l,I as j,S as A,T as x,O as G,y as H,Q as O,_ as Q}from"./index-Ba8PhKSB.js";import{g as U,r as Y}from"./favorite-DTGhV1c6.js";import{a as J}from"./cart-Cx8k0y7Y.js";import"./user-CswI-_sj.js";const K={class:"favorites-container"},W={class:"page-header"},X={class:"header"},Z={class:"title-wrapper"},ee={class:"count"},se={class:"favorites-content"},te={class:"product-content"},oe=["onClickPassive"],ae={class:"image-slot"},ce={class:"product-info"},re={class:"product-name"},ne={class:"product-description"},le={class:"product-footer"},de={class:"price-stock"},ie={class:"price"},ue={class:"product-actions"},_e=V({__name:"favorites",setup(pe){const y=R(),d=h([]),v=h(!0),i=h(null),P=async()=>{try{v.value=!0;const s=await U();if(s.data){const e=s.data;e.code===200?d.value=e.data:r.error(e.message||"获取收藏列表失败")}}catch{r.error("获取收藏列表失败")}finally{v.value=!1}},B=async s=>{try{await O.confirm("确定要取消收藏这个商品吗？","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}),i.value=s.id;const e=await Y(s.id);if(e.data){const n=e.data;n.code===200?(d.value=d.value.filter(u=>u.id!==s.id),r.success("取消收藏成功")):r.error(n.message||"取消收藏失败")}}catch(e){e!=="cancel"&&r.error("取消收藏失败")}finally{i.value=null}},I=async s=>{if(s.product.stock<=0){r.warning("商品库存不足");return}try{i.value=s.id;const e={productId:s.productId,quantity:1},n=await J(e);if(n.data){const u=n.data;u.code===200?r.success("添加到购物车成功"):r.error(u.message||"添加到购物车失败")}else r.error("添加到购物车失败")}catch(e){r.error((e==null?void 0:e.message)||"添加到购物车失败")}finally{i.value=null}},T=s=>s==null?"¥0.00":s.toLocaleString("zh-CN",{style:"currency",currency:"CNY"}),b=s=>{y.push(`/product/${s.productId}`)};return S(()=>{P()}),(s,e)=>{const n=l("el-button"),u=l("el-empty"),g=l("el-icon"),F=l("el-image"),N=l("el-tag"),$=l("el-card"),D=l("el-col"),E=l("el-row"),M=j("loading");return _(),w("div",K,[t("div",W,[t("div",X,[t("div",Z,[e[1]||(e[1]=t("span",{class:"title"},"我的收藏",-1)),t("span",ee,"共 "+p(d.value.length)+" 件商品",1)])])]),t("div",se,[!v.value&&d.value.length===0?(_(),m(u,{key:0,description:"暂无收藏商品"},{extra:a(()=>[c(n,{type:"primary",onClickPassive:e[0]||(e[0]=o=>f(y).push("/products"))},{default:a(()=>e[2]||(e[2]=[C(" 去逛逛 ")])),_:1})]),_:1})):q((_(),m(E,{key:1,gutter:20},{default:a(()=>[(_(!0),w(z,null,L(d.value,o=>(_(),m(D,{xs:24,sm:12,md:8,lg:6,key:o.id},{default:a(()=>[c($,{class:"favorite-card",shadow:"hover"},{default:a(()=>[t("div",te,[t("div",{class:"product-main",onClickPassive:k=>b(o)},[c(F,{src:o.product.imageUrl||"/default-product.jpg",alt:o.product.name,class:"product-image",fit:"cover"},{error:a(()=>[t("div",ae,[c(g,null,{default:a(()=>[c(f(A))]),_:1})])]),_:2},1032,["src","alt"]),t("div",ce,[t("div",re,p(o.product.name),1),t("div",ne,p(o.product.description),1)])],40,oe),t("div",le,[t("div",de,[t("span",ie,p(T(o.product.price)),1),c(N,{type:o.product.stock>0?"success":"error",size:"small",effect:"plain"},{default:a(()=>[C(p(o.product.stock>0?"有货":"无货"),1)]),_:2},1032,["type"])]),t("div",ue,[c(n,{circle:"",type:"danger",onClickPassive:x(k=>B(o),["stop"]),loading:i.value===o.id},{default:a(()=>[c(g,null,{default:a(()=>[c(f(G))]),_:1})]),_:2},1032,["onClickPassive","loading"]),c(n,{circle:"",type:"primary",onClickPassive:x(k=>I(o),["stop"]),loading:i.value===o.id,disabled:o.product.stock<=0},{default:a(()=>[c(g,null,{default:a(()=>[c(f(H))]),_:1})]),_:2},1032,["onClickPassive","loading","disabled"])])])])]),_:2},1024)]),_:2},1024))),128))]),_:1})),[[M,v.value]])])])}}}),me=Q(_e,[["__scopeId","data-v-1d89c518"]]);export{me as default};
