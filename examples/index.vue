<template>
    <div>
        <div style="flex-direction: row;">
            <text class="button" @click="onLoadFile('/sdcard/qhk.trc')">file1</text>
            <text class="button" @click="onLoadFile('/sdcard/noFile.trc')">file2</text>

            <text class="button" @click="onChangeColor('#bb0101', '#d4731b')">color1</text>
            <text class="button" @click="onChangeColor('#0d4bb1', '#c0b60e')">color2</text>

            <text class="button" @click="onChangePlayState">动起来</text>

        </div>
        <lyric style="flex: 1; background-color: gray;" :lyricFile="lyricFile" :highlightColor="highlightColor" :normalColor="normalColor"
         :kalaOK="kalaOK" :playStatus="playState" :textAlign="textAlign" :slowScroll="slowScroll"></lyric>
        <div style="flex-direction: row; align-items: center;">
            <text>TextAlign:</text>
            <text class="button" @click="onChangeTextAlign(0)">left</text>
            <text class="button" @click="onChangeTextAlign(1)">center</text>
            <text class="button" @click="onChangeTextAlign(2)">right</text>
        </div>
        <!--<div style="flex-direction: row; align-items: center;">-->
            <!--<text >DisplayMode: </text>-->
            <!--<text class="button" @click="onChangeDisplayMode(0)">Normal</text>-->
            <!--<text class="button" @click="onChangeDisplayMode(1)">MTV</text>-->
            <!--<text class="button" @click="onChangeDisplayMode(2)">Single</text>-->
        <!--</div>-->
        <div style="flex-direction: row; align-items: center;">
            <text class="button" @click="onChangeKalaOK">kalaOk</text>
            <text class="button" @click="onChangeSlowScroll">缓慢滚动</text>
        </div>
    </div>
</template>

<script>
    export default {
        data: {
            lyricFile: '',
            highlightColor: '',
            normalColor: '',
            kalaOK: true,
            slowScroll: true,
            textAlign: 1,
            displayMode: 0,
            playState: false
        },
        methods: {
            onLoadFile: function (file) {
                this.lyricFile = file;
            },
            onChangeColor: function (highColor, normalColor) {
                this.highlightColor = highColor;
                this.normalColor = normalColor;
            },
            onChangeKalaOK: function () {
                this.kalaOK = !this.kalaOK;
            },
            onChangeSlowScroll: function () {
                this.slowScroll = !this.slowScroll;
            },
            onChangeTextAlign: function (align) {
                this.textAlign = align;
            },
            onChangeDisplayMode: function (displayMode) {
                this.displayMode = displayMode;
            },
            onChangePlayState: function () {
                this.playState = !this.playState;
            }
        }
    }
</script>

<style>
    .button {
        background-color: aquamarine;
        font-size: 40;
        margin: 10;
        padding: 10;
    }
</style>
