/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.flowcentraltech.flowcentral.application.web.writers;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.Comment;
import com.flowcentraltech.flowcentral.application.web.widgets.CommentListing;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.format.Formatter;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractWidgetWriter;

/**
 * Comment listing.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(CommentListing.class)
@Component("commentlisting-writer")
public class CommentListingWriter extends AbstractWidgetWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        CommentListing commentListing = (CommentListing) widget;
        writer.write("<div ");
        writeTagAttributes(writer, commentListing);
        writer.write("><div class=\"ctable\">");

        List<Comment> comments = commentListing.getComments();
        if (!DataUtils.isBlank(comments)) {
            Formatter<?> formatter = commentListing.getTimestampFormatter();
            final int len = comments.size();
            for (int i = 0; i < len; i++) {
                writer.write("<div class=\"");
                if (i % 2 == 0) {
                    writer.write("codd");
                } else {
                    writer.write("ceven");
                }
                writer.write("\">");
                writer.write("<div class=\"cserialno\">").write(i + 1).write(".</div>");

                writer.write("<div class=\"cbody\">");
                writer.write("<div style=\"display:table;width:100%;\">");
                Comment comment = comments.get(i);
                writeCommentPart(writer, "ccontent", comment.getMessage());
                writeCommentPart(writer, "clabel",
                        getSessionMessage("commentlisting.user.action", comment.getCommentBy(), comment.getAction()));
                writeCommentPart(writer, "clabel", DataUtils.convert(String.class, comment.getTimestamp(), formatter));
                writer.write("</div>");
                writer.write("</div>");
                writer.write("</div>");
            }
        }

        writer.write("</div></div>");
    }

    private void writeCommentPart(ResponseWriter writer, String styleClass, String part) throws UnifyException {
        writer.write("<div style=\"display:table-row;\">");
        writer.write("<div class=\"").write(styleClass).write("\"><span>");
        if (part != null) {
            writer.writeWithHtmlEscape(part);
        }
        writer.write("</span></div>");
        writer.write("</div>");
    }

}
