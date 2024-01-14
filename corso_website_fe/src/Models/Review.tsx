class Review {
    public customerFullName: string;
    public message: string;
    public reviewRating: number;
    public pinned: boolean;

    constructor(
        customerFullName: string,
        message: string,
        reviewRating: number,
        pinned: boolean
    ) {
        this.customerFullName = customerFullName;
        this.message = message;
        this.reviewRating = reviewRating;
        this.pinned = pinned;
    }
}

export default Review;