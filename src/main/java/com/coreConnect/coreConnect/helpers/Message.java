package com.coreConnect.coreConnect.helpers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class Message {
private String content;
@Builder.Default
private MessageType type = MessageType.green;

}
