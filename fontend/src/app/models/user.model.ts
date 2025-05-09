
export interface User{
    userId: number;
    email: string;
    name: string;
    surname: string;
    birthday: Date;
    wishListId: string;
    banned?: false;
}
