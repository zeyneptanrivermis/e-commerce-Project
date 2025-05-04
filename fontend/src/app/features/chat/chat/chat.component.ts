import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ChatService } from '../service/chat.service';

@Component({
  selector: 'app-chat',
  standalone: false,
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent {
  hover = false;
  chatOpen = false;

  chatForm: FormGroup;

  messages: { text: string; sender: 'user' | 'bot' }[] = [];

  constructor(private fb: FormBuilder, private chatService: ChatService) {
    this.chatForm = this.fb.group({
      message: ['']
    });
  }

  toggleChat() {
    this.chatOpen = !this.chatOpen;
  }

  sendMessage() {
    const msg = this.chatForm.value.message;
    if (!msg.trim()) return;

    this.messages.push({ text: msg, sender: 'user' });

    this.chatService.sendMessage(msg).subscribe((res: any) => {
      this.messages.push({ text: res.reply, sender: 'bot' });
    });

    this.chatForm.reset();
  }
}
