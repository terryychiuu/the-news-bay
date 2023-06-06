package com.example.thenewsbay.feature_news.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thenewsbay.R;
import com.example.thenewsbay.feature_news.models.room.account.Newspaper;
import com.example.thenewsbay.feature_news.models.room.account.User;
import com.example.thenewsbay.feature_news.models.room.saved_article.Article;
import com.example.thenewsbay.feature_news.ui.WebViewActivity;
import com.example.thenewsbay.feature_news.viewModel.AccountViewModel;
import com.example.thenewsbay.feature_news.viewModel.SavedArticleViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class NewspaperAdapter extends RecyclerView.Adapter<NewspaperAdapter.NewspaperViewHolder>{
    AccountViewModel accountViewModel;
    SavedArticleViewModel savedArticleViewModel;

    private final Context context;
    private List<Newspaper> newspaperArrayList = new ArrayList<>();

    public NewspaperAdapter(Context context) {
        this.context = context;
    }

    public static class NewspaperViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle, mDescription, mAuthor, mPublishedAt, mCategory;
        ImageView mImage, mSave, mShare, mDownload;
        CardView cardView;

        public NewspaperViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.tv_article_title);
            mDescription = itemView.findViewById(R.id.tv_article_description);
            mAuthor = itemView.findViewById(R.id.tv_article_author);
            mPublishedAt = itemView.findViewById(R.id.tv_article_publishedAt);
            mImage = itemView.findViewById(R.id.iv_article_image);
            mSave = itemView.findViewById(R.id.iv_article_save);
            mShare = itemView.findViewById(R.id.iv_article_share);
            mDownload = itemView.findViewById(R.id.iv_article_download);
            cardView = itemView.findViewById(R.id.cv_article_card);
        }
    }

    @NonNull
    @Override
    public NewspaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new NewspaperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewspaperViewHolder holder, int position) {
        Newspaper newspaper = newspaperArrayList.get(position);
        String url = newspaper.getUrl();
        String title = newspaper.getTitle();
        String description = newspaper.getDescription();
        String authorText = (newspaper.getAuthor() != null && !newspaper.getAuthor().trim().equals("") ? "By " + newspaper.getAuthor() : "");
        String content = newspaper.getContent();
        Date publishedAt = newspaper.getPublishedAt();
        String urlToImage = newspaper.getUrlToImage();

        holder.mTitle.setText(title);
        holder.mDescription.setText(description);
        holder.mAuthor.setText(authorText);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy - K:mm a");
        holder.mPublishedAt.setText(publishedAt.toString());
        // web view
        Glide.with(context).load(urlToImage).into(holder.mImage);

        if (accountViewModel != null) {
            accountViewModel.getAllNewspapers().observe((LifecycleOwner) context, new Observer<List<Newspaper>>() {
                @Override
                public void onChanged(List<Newspaper> newspapers) {
                    boolean isSaved = newspapers.stream().anyMatch(newspaper -> newspaper.url.equals(url));
                    if (isSaved)
                        holder.mSave.setImageResource(R.drawable.ic_saved);
                    else
                        holder.mSave.setImageResource(R.drawable.ic_save);
                }
            });
        }

        holder.mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accountViewModel != null) {
                    accountViewModel.getUser().observe((LifecycleOwner) context, new Observer<List<User>>() {
                        @Override
                        public void onChanged(List<User> users) {
                            if(users.size() == 1) {
                                List<Newspaper> newspapers = accountViewModel.getAllNewspapersStatic();
                                boolean isSaved = newspapers.stream().anyMatch(newspaper -> newspaper.url.equals(url));
                                if (isSaved) {
                                    Toasty.success(view.getContext(), "Unsaved!", Toast.LENGTH_SHORT).show();

                                    accountViewModel.deleteNewspaper(new Newspaper(url, authorText, title, description, urlToImage, publishedAt, content));
                                    savedArticleViewModel.deleteReaderArticleCrossRdf(users.get(0).email, url);
                                }
                                else {
                                    Toasty.success(view.getContext(), "Added!", Toast.LENGTH_SHORT).show();

                                    accountViewModel.insertNewspaper(new Newspaper(url, authorText, title, description, urlToImage, publishedAt, content));
                                    savedArticleViewModel.insertArticle(new Article(url, authorText, title, description, urlToImage, publishedAt, content));
                                    savedArticleViewModel.insertReaderArticleCrossRdf(users.get(0).email, url);
                                }

//                                holder.mSave.setImageResource(R.drawable.ic_saved);
                            } else {
                                Toasty.error(view.getContext(), "You need to login first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        holder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                shareIntent.putExtra(Intent.EXTRA_TEXT, url);

                context.startActivity(Intent.createChooser(shareIntent, "Share article via"));
            }
        });

        holder.mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileTitle = title.replaceAll("[^A-Za-z0-9_\\\\-]", "_");
                String fileContent = title + "\n" +
                        authorText + "\n\n" +
                        description + "\n\n" +
                        content + "\n\n" +
                        publishedAt + "\n" +
                        url;

                String downloadDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

                File file = new File(downloadDirPath, fileTitle + ".txt");

                try {
                    // Create a new FileOutputStream for the new file in the Downloads directory
                    FileOutputStream outputStream = new FileOutputStream(file);

                    // Write the file content to the FileOutputStream
                    outputStream.write(fileContent.getBytes());

                    // Flush and close the FileOutputStream
                    outputStream.flush();
                    outputStream.close();

                    // Show a toast message indicating the file was saved
                    Toast.makeText(context, "File saved to Downloads folder", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    // Show a toast message indicating an error occurred
                    Toast.makeText(context, "Error saving file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url", newspaper.getUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newspaperArrayList.size();
    }

    public void setNewspaperArrayList(List<Newspaper> newspaperArrayList) {
        this.newspaperArrayList = newspaperArrayList;
        notifyDataSetChanged();
    }

    public void setAccountViewModel(AccountViewModel accountViewModel) {
        this.accountViewModel = accountViewModel;
    }

    public void setSavedArticleViewModel(SavedArticleViewModel savedArticleViewModel) {
        this.savedArticleViewModel = savedArticleViewModel;
    }
}
