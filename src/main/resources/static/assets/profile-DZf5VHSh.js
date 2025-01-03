import{d as q,r as x,n as z,E as n,o as _,c as P,b as o,w as e,e as u,f as a,u as p,t as c,q as v,G,H,h as f,U as C,M as R,R as j,_ as J}from"./index-D07GUNLX.js";import{u as K}from"./user-B44x33Ff.js";import{a as O}from"./address-DxOjfI-V.js";import{f as D}from"./date-D_0JMZE-.js";const Q={class:"profile-container"},W={class:"card-header"},X={class:"avatar-section"},Y={class:"card-header"},Z={class:"header-actions"},ee={class:"address-actions"},te=q({__name:"profile",setup(re){const d=K(),h=x(),k=x([]);z(async()=>{try{await Promise.all([d.fetchUserProfile(),b()])}catch{n.error("获取信息失败")}});const b=async()=>{try{const l=await O.getAddressList();k.value=l.data.data}catch(l){console.error("获取地址列表失败:",l),n.error("获取地址列表失败")}},B=async()=>{try{await b(),n.success("刷新地址列表成功")}catch{n.error("刷新地址列表失败")}},E=async()=>{try{await d.fetchUserProfile(),n.success("刷新成功")}catch{n.error("刷新失败")}},T=()=>{var l;(l=h.value)==null||l.click()},U=async l=>{var y;const r=l.target,i=(y=r.files)==null?void 0:y[0];if(i)try{await d.uploadAvatar(i),n.success("头像上传成功")}catch(s){n.error(s.message||"头像上传失败")}finally{r.value=""}},M=l=>`${l.province}${l.city}${l.district} ${l.detailAddress} ${l.isDefault?"(默认)":""}`,g=()=>{n.info("添加地址功能开发中...")},N=l=>{n.info("编辑地址功能开发中...")},I=l=>{j.confirm("确定要删除这个地址吗？","删除确认",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(()=>{n.info("删除地址功能开发中...")}).catch(()=>{})},S=l=>{n.info("设置默认地址功能开发中...")};return(l,r)=>{const i=f("el-button"),y=f("el-avatar"),s=f("el-descriptions-item"),A=f("el-descriptions"),$=f("el-card"),V=f("el-empty"),F=f("el-collapse-item"),L=f("el-collapse");return _(),P("div",Q,[o($,{class:"profile-card"},{header:e(()=>[u("div",W,[r[1]||(r[1]=u("span",null,"个人信息",-1)),o(i,{type:"primary",onClick:E},{default:e(()=>r[0]||(r[0]=[a("刷新")])),_:1})])]),default:e(()=>[u("div",X,[o(y,{size:100,src:p(d).avatarUrl,onError:()=>!0},{default:e(()=>{var t,m,w;return[a(c((w=(m=(t=p(d).userProfile)==null?void 0:t.name)==null?void 0:m.charAt(0))==null?void 0:w.toUpperCase()),1)]}),_:1},8,["src"]),o(i,{type:"primary",onClick:T,class:"upload-btn"},{default:e(()=>r[2]||(r[2]=[a(" 更换头像 ")])),_:1})]),o(A,{column:1,border:""},{default:e(()=>[o(s,{label:"用户ID"},{default:e(()=>{var t;return[a(c((t=p(d).userProfile)==null?void 0:t.id),1)]}),_:1}),o(s,{label:"用户名"},{default:e(()=>{var t;return[a(c((t=p(d).userProfile)==null?void 0:t.name),1)]}),_:1}),o(s,{label:"邮箱"},{default:e(()=>{var t;return[a(c((t=p(d).userProfile)==null?void 0:t.email),1)]}),_:1})]),_:1})]),_:1}),o($,null,{header:e(()=>[u("div",Y,[r[5]||(r[5]=u("span",null,"收货地址",-1)),u("div",Z,[o(i,{type:"primary",onClick:g},{default:e(()=>r[3]||(r[3]=[a("添加地址")])),_:1}),o(i,{type:"primary",onClick:B},{default:e(()=>r[4]||(r[4]=[a("刷新")])),_:1})])])]),default:e(()=>[k.value.length?(_(),v(L,{key:1},{default:e(()=>[(_(!0),P(G,null,H(k.value,t=>(_(),v(F,{key:t.id,title:M(t)},{extra:e(()=>[u("div",ee,[t.isDefault?R("",!0):(_(),v(i,{key:0,type:"primary",link:"",onClick:C(m=>S(t),["stop"])},{default:e(()=>r[7]||(r[7]=[a(" 设为默认 ")])),_:2},1032,["onClick"])),o(i,{type:"primary",link:"",onClick:C(m=>N(t),["stop"])},{default:e(()=>r[8]||(r[8]=[a(" 编辑 ")])),_:2},1032,["onClick"]),o(i,{type:"danger",link:"",onClick:C(m=>I(t),["stop"])},{default:e(()=>r[9]||(r[9]=[a(" 删除 ")])),_:2},1032,["onClick"])])]),default:e(()=>[o(A,{column:1,border:""},{default:e(()=>[o(s,{label:"收货人"},{default:e(()=>[a(c(t.receiverName),1)]),_:2},1024),o(s,{label:"联系电话"},{default:e(()=>[a(c(t.receiverPhone),1)]),_:2},1024),o(s,{label:"所在地区"},{default:e(()=>[a(c(`${t.province}${t.city}${t.district}`),1)]),_:2},1024),o(s,{label:"详细地址"},{default:e(()=>[a(c(t.detailAddress),1)]),_:2},1024),o(s,{label:"是否默认"},{default:e(()=>[a(c(t.isDefault?"是":"否"),1)]),_:2},1024),o(s,{label:"创建时间"},{default:e(()=>[a(c(p(D)(t.createTime)),1)]),_:2},1024),o(s,{label:"更新时间"},{default:e(()=>[a(c(p(D)(t.updateTime)),1)]),_:2},1024)]),_:2},1024)]),_:2},1032,["title"]))),128))]),_:1})):(_(),v(V,{key:0,description:"暂无收货地址"},{default:e(()=>[o(i,{type:"primary",onClick:g},{default:e(()=>r[6]||(r[6]=[a("添加收货地址")])),_:1})]),_:1}))]),_:1}),u("input",{type:"file",ref_key:"fileInput",ref:h,style:{display:"none"},accept:"image/*",onChange:U},null,544)])}}}),ne=J(te,[["__scopeId","data-v-01d227dd"]]);export{ne as default};