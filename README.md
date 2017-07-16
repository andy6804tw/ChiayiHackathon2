# ChiayiHackathon2
2017 嘉義黑蚵松(地方開放資料黑克松-環境汙染)

## App Screenshot 
<img src="Screenshot/20170716_124745_edited.gif" width="300">

```
  台灣是個數據開放的社會，我們以最貼近民眾的方向去思考，決定撈取天氣環境的公開資料，此外由於現今是人手一機的時代，
所以我們利用這些資訊打包成App供民眾可以下載使用，並以圖像視覺化的方式呈現給使用者，雖然市面上也有許多相近的天氣App，
因此我們不僅僅只是呈現這些環境資訊，我們還特別加入了天氣環境對於健康的影響，貼心的提醒使用者活動建議以及防護措施，
並與現有的產作出區隔。
  在本次嘉義黑蚵松當中除了使用手機App呈現空汙即時警訊，我們發揮創意使用了Webduino物聯網開發版結合LED燈顯示目前空氣品質
顏色，除此之外還結合語音的功能與使用者互動例如詢問目前的天氣狀況、特定縣市的空氣品質狀況
```

### 本活動所使用到的資料集:<br>
<a href="http://opendata.epa.gov.tw//">行政院環境保護署</a><br>
<a href="http://opendata.epa.gov.tw/Data/Contents/AQXSite/">空氣品質測站基本資料</a><br>
<a href="http://opendata.epa.gov.tw/Data/Contents/AQI/">空氣品質指標(AQI)</a><br>
<a href="http://opendata.cwb.gov.tw/datalist">交通部中央氣象局</a><br>
<a href="http://data.chiayi.gov.tw/opendata/dataset/metadata?oid=729e3dbe-bc48-49d3-9677-7fb8774096f8">嘉義市政府資料開放平台- 雲嘉嘉空氣盒子API</a><br>
### 自行建立的資料集:
#### 說明:
我們使用Node.js背景撈取行政院環境保護署的空氣品質指標(AQI)每小時的空氣數值，並可查詢近幾日來每天的AQI濃度
<a href="https://github.com/andy6804tw/Node_Firebase">Node_Firebase Repositories</a><br>
<a href="https://andy6804tw.github.io/Node_Firebase/OpenDataAQI.html">資料存取網址</a><br>

<img src="app/src/main/res/mipmap-xhdpi/icon.png" width="100">
空汙小幫手

<a href="https://icons8.com/icon/12128/Facepalm">Facepalm icon credits</a><br>
<a href="http://www.freepik.com/free-vector/happy-kids-illustration_828943.htm">Designed by Freepik</a>
