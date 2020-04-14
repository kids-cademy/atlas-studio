$package("com.kidscademy");

com.kidscademy.Md2Htm = class {
    constructor(markdown) {
        this._markdown = markdown;
        // a minor simplification on markdown parser logic: ensure last line has proper line end
        if (this._markdown != null && !this._markdown.endsWith("\r\n")) {
            this._markdown += "\r\n";
        }

        this._html = "";

        this._inlineTag = null;
    }

    convert() {
        if (this._markdown == null) {
            // markdown can be empty if 
            return;
        }

        // 0 LINE_START
        // 1 SECOND_HASH
        // 2 WAIT_DOT
        // 3 SKIP_SPACE
        // 4 COLLECT_LINE
        // 5 TABLE_HEADER
        // 6 TABLE_UNDERLINE
        // 7 TABLE_BODY
        // 8 TABLE_BODY_END

        var blockTag = null;
        var listTag = null;
        var cellIndex = 0;
        var state = 0; // LINE_START

        for (let i = 0; i < this._markdown.length; ++i) {
            const c = this._markdown.charAt(i);
            if (c === '\r') {
                continue;
            }

            switch (state) {
                case 0: // LINE_START
                    switch (c) {
                        case '#':
                            state = 1; // SECOND_HASH
                            break;

                        case '|':
                            this._html += "<table><tr><th>";
                            state = 5; // TABLE_HEADER
                            break;

                        case '-':
                            if (listTag == null) {
                                listTag = "ul";
                                this._html += "<ul>";
                            }
                            this._html += "<li>";
                            state = 3; // SKIP SPACE
                            break;

                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            state = 2; // WAIT_DOT
                            break;

                        case '\n':
                            // ignore empty lines
                            break;

                        default:
                            if (listTag != null) {
                                this._html += `</${listTag}>`;
                                listTag = null;
                            }
                            blockTag = "p";
                            this._html += "<p>";
                            this._append(c);
                            state = 4; // COLLECT_LINE
                            break;
                    }
                    break;

                case 1: // SECOND_HASH
                    if (c === '#') {
                        blockTag = "h2";
                        this._html += "<h2>";
                    }
                    else {
                        blockTag = "h1";
                        this._html += "<h1>";
                    }
                    state = 3; // SKIP_SPACE
                    break;

                case 2: // WAIT_DOT
                    if (c === '.') {
                        if (listTag == null) {
                            listTag = "ol";
                            this._html += "<ol>";
                        }
                        this._html += "<li>";
                        state = 3; // SKIP_SPACE
                    }
                    break;

                case 3: // SKIP_SPACE
                    if (c === ' ') {
                        break;
                    }
                    state = 4; // COLLECT_LINE
                // fall through next case

                case 4: // COLLECT_LINE
                    if (c === '\n') {
                        // take care to close inline tag from current line if markdown source forgets closing it
                        if (this._inlineTag != null) {
                            this._html += `</${this._inlineTag}>`;
                            this._inlineTag = null;
                        }
                        if (listTag != null) {
                            this._html += "</li>";
                        }
                        if (blockTag != null) {
                            this._html += `</${blockTag}>`;
                            blockTag = null;
                        }
                        state = 0; // LINE_START
                        break;
                    }
                    this._append(c);
                    break;

                case 5: // TABLE_HEADER
                    if (c === '\n') {
                        this._html += "</th></tr>";
                        state = 6; // TABLE_UNDERLINE
                        break;
                    }
                    if (c === '|') {
                        this._html += "</th><th>";
                        break;
                    }
                    this._append(c);
                    break;

                case 6: // TABLE_UNDERLINE
                    if (c === '\n') {
                        cellIndex = 0;
                        state = 7; // TABLE_BODY
                    }
                    break;

                case 8: // TABLE_BODY_END
                    if (c === '\n') {
                        this._html += "</table>";
                        state = 0; // LINE_START
                        break;
                    }
                    state = 7; // TABLE_BODY
                // fall through next case

                case 7: // TABLE_BODY
                    if (c === '\n') {
                        this._html += "</td></tr>";
                        cellIndex = 0;
                        state = 8; // TABLE_BODY_END
                        break;
                    }
                    if (c === '|') {
                        if (cellIndex++ === 0) {
                            this._html += "<tr>";
                        }
                        else {
                            this._html += "</td>";
                        }
                        this._html += "<td>";
                        break;
                    }
                    this._append(c);
                    break;
            }

        }

        return this._html;
    }

    _append(char) {
        switch (char) {
            case '&':
                this._html += "&amp;"
                break;

            case '<':
                this._html += "&lt;";
                break;

            case '>':
                this._html += "&gt;";
                break;

            case '"':
                if (this._inlineTag == null) {
                    this._html += "<em>";
                    this._inlineTag = "em";
                }
                else {
                    this._html += `</${this._inlineTag}>`;
                    this._inlineTag = null;
                }
                break;

            case '`':
                if (this._inlineTag == null) {
                    this._html += "<strong>";
                    this._inlineTag = "strong";
                }
                else {
                    this._html += `</${this._inlineTag}>`;
                    this._inlineTag = null;
                }
                break;

            default:
                this._html += char;
        }
    }

    toString() {
        return "com.kidscademy.Md2Htm";
    }
};
