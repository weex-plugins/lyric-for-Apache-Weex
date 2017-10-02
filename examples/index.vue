<template>
    <div>
        <div style="flex-direction: row;">
            <text class="button" @click="onLoadFile('/sdcard/qhk.trc')">file1</text>
            <text class="button" @click="onLoadFile('/sdcard/noFile.trc')">file2</text>

            <text class="button" @click="onChangeColor('#FF0000', '#00FF00')">color1</text>
            <text class="button" @click="onChangeColor('#0000FF', '#FFFFFF')">color2</text>
        </div>
        <lyric style="flex: 1; background-color: brown;" :lyricFile="lyricFile"></lyric>
        <div style="flex-direction: row;">
            <text class="button" style="color: #000;">bottom</text>
        </div>
    </div>
</template>

<script>
    export default {
        data: {
            lyricFile: '',
            highlightColor: '',
            normalColor: '',
        },
        methods: {
            onLoadFile: function (file) {
                this.lyricFile = file;
            },
            onChangeColor: function (highColor, normalColor) {
                this.highlightColor = highColor;
                this.normalColor = normalColor;
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
