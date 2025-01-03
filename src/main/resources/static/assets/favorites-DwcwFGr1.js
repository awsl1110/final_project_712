import{d as z,r as h,n as R,o as p,c as w,b as a,w as c,e as o,t as _,q as m,f as C,u as v,I as U,G as q,H as L,g as S,E as r,h as d,J as j,N as A,U as x,Q as G,y as H,R as J,_ as P}from"./index-D07GUNLX.js";import{g as Q,r as Y}from"./favorite-Bs-eO1ak.js";import{a as K}from"./cart-BDXYFz_L.js";import"./user-B44x33Ff.js";const O={class:"favorites-container"},W={class:"header"},X={class:"count"},Z={class:"favorites-content"},ee={class:"product-content"},te=["onClick"],se={class:"image-slot"},oe={class:"product-info"},ae={class:"product-name"},ce={class:"product-description"},re={class:"product-footer"},ne={class:"price-stock"},le={class:"price"},de={class:"product-actions"},ie=z({__name:"favorites",setup(ue){const y=S(),i=h([]),f=h(!0),u=h(null),B=async()=>{try{f.value=!0;const t=await Q();if(t.data){const e=t.data;e.code===200?i.value=e.data:r.error(e.message||"获取收藏列表失败")}}catch{r.error("获取收藏列表失败")}finally{f.value=!1}},b=async t=>{try{await J.confirm("确定要取消收藏这个商品吗？","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}),u.value=t.id;const e=await Y(t.id);if(e.data){const n=e.data;n.code===200?(i.value=i.value.filter(l=>l.id!==t.id),r.success("取消收藏成功")):r.error(n.message||"取消收藏失败")}}catch(e){e!=="cancel"&&r.error("取消收藏失败")}finally{u.value=null}},I=async t=>{if(t.product.stock<=0){r.warning("商品库存不足");return}try{u.value=t.id;const e={productId:t.productId,quantity:1},n=await K(e);if(n.data){const l=n.data;l.code===200?r.success("添加到购物车成功"):r.error(l.message||"添加到购物车失败")}else r.error("添加到购物车失败")}catch(e){r.error((e==null?void 0:e.message)||"添加到购物车失败")}finally{u.value=null}},N=t=>t==null?"¥0.00":t.toLocaleString("zh-CN",{style:"currency",currency:"CNY"}),T=t=>{y.push(`/product/${t.productId}`)};return R(()=>{B()}),(t,e)=>{const n=d("el-card"),l=d("el-button"),$=d("el-empty"),g=d("el-icon"),D=d("el-image"),E=d("el-tag"),F=d("el-col"),M=d("el-row"),V=j("loading");return p(),w("div",O,[a(n,{class:"page-header"},{header:c(()=>[o("div",W,[e[1]||(e[1]=o("span",{class:"title"},"我的收藏",-1)),o("span",X,"共 "+_(i.value.length)+" 件商品",1)])]),_:1}),o("div",Z,[!f.value&&i.value.length===0?(p(),m($,{key:0,description:"暂无收藏商品"},{default:c(()=>[a(l,{type:"primary",onClick:e[0]||(e[0]=s=>v(y).push("/products"))},{default:c(()=>e[2]||(e[2]=[C(" 去逛逛 ")])),_:1})]),_:1})):U((p(),m(M,{key:1,gutter:20},{default:c(()=>[(p(!0),w(q,null,L(i.value,s=>(p(),m(F,{xs:24,sm:12,md:8,lg:6,key:s.id},{default:c(()=>[a(n,{class:"favorite-card","body-style":{padding:"0px"}},{default:c(()=>[o("div",ee,[o("div",{class:"product-main",onClick:k=>T(s)},[a(D,{src:s.product.imageUrl||"/default-product.jpg",alt:s.product.name,class:"product-image",fit:"cover","preview-src-list":[s.product.imageUrl]},{error:c(()=>[o("div",se,[a(g,{size:24},{default:c(()=>[a(v(A))]),_:1})])]),_:2},1032,["src","alt","preview-src-list"]),o("div",oe,[o("h3",ae,_(s.product.name),1),o("p",ce,_(s.product.description),1)])],8,te),o("div",re,[o("div",ne,[o("span",le,_(N(s.product.price)),1),a(E,{type:s.product.stock>0?"success":"error",size:"small",effect:"light"},{default:c(()=>[C(_(s.product.stock>0?"有货":"无货"),1)]),_:2},1032,["type"])]),o("div",de,[a(l,{circle:"",type:"danger",onClick:x(k=>b(s),["stop"]),loading:u.value===s.id},{default:c(()=>[a(g,null,{default:c(()=>[a(v(G))]),_:1})]),_:2},1032,["onClick","loading"]),a(l,{circle:"",type:"primary",onClick:x(k=>I(s),["stop"]),loading:u.value===s.id,disabled:s.product.stock<=0},{default:c(()=>[a(g,null,{default:c(()=>[a(v(H))]),_:1})]),_:2},1032,["onClick","loading","disabled"])])])])]),_:2},1024)]),_:2},1024))),128))]),_:1})),[[V,f.value]])])])}}}),ge=P(ie,[["__scopeId","data-v-2cc4ff97"]]);export{ge as default};
