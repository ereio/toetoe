package com.ToeTactics.tictactictactoe;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GameBoardFragment extends Fragment {

	ImageView[][][][] spaces = new ImageView[3][3][3][3];
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state){
		
		return inflater.inflate(R.layout.game_board, container);
	}
	
	public void makeMove(int i, int j, int k, int l){
		//if(board.makeMove(i, j, k, l)){
		//mark last player since that's who made the move
		//if(board.current_player = 'O'){
			spaces[i][j][k][l].setImageResource(R.drawable.x_tile_w_bg);
		//}
		//if(board.current_player = 'X'){
			//spaces[i][j][k][l].setImageResource(R.drawable.o_tile_w_bg);
		//}
	//}
	}
	
	@Override
	public void onStart(){
		super.onStart();
		//Board Coordinates Reference
		//        0         1         2
		//     0  1  2   0  1  2   0  1  2
		//
		//  0  00|10|20  30|40|50  60|70|80
		//     --------  --------  --------
		//0 1  01|11|21  31|41|51  61|71|81
		//     --------  --------  --------
		//  2  02|12|22  32|42|52  62|72|82
		//
		//  0  03|13|23  33|43|53  63|73|83
		//     --------  --------  --------
		//1 1  04|14|24  34|44|54  64|74|84
		//     --------  --------  --------
		//  2  05|15|25  35|45|55  65|75|85
		//
		//  0  06|16|26  36|46|56  66|76|86
		//     --------  --------  --------
		//2 1  07|17|27  37|47|57  67|77|87
		//     --------  --------  --------
		//  2  08|18|28  38|48|58  68|78|88
		//
		
		spaces[0][0][0][0] = (ImageView) getActivity().findViewById(R.id.space00);
		spaces[0][0][0][1] = (ImageView) getActivity().findViewById(R.id.space01);
		spaces[0][0][0][2] = (ImageView) getActivity().findViewById(R.id.space02);
		spaces[0][0][1][0] = (ImageView) getActivity().findViewById(R.id.space10);
		spaces[0][0][1][1] = (ImageView) getActivity().findViewById(R.id.space11);
		spaces[0][0][1][2] = (ImageView) getActivity().findViewById(R.id.space12);
		spaces[0][0][2][0] = (ImageView) getActivity().findViewById(R.id.space20);
		spaces[0][0][2][1] = (ImageView) getActivity().findViewById(R.id.space21);
		spaces[0][0][2][2] = (ImageView) getActivity().findViewById(R.id.space22);
		
		spaces[0][1][0][0] = (ImageView) getActivity().findViewById(R.id.space03);
		spaces[0][1][0][1] = (ImageView) getActivity().findViewById(R.id.space04);
		spaces[0][1][0][2] = (ImageView) getActivity().findViewById(R.id.space05);
		spaces[0][1][1][0] = (ImageView) getActivity().findViewById(R.id.space13);
		spaces[0][1][1][1] = (ImageView) getActivity().findViewById(R.id.space14);
		spaces[0][1][1][2] = (ImageView) getActivity().findViewById(R.id.space15);
		spaces[0][1][2][0] = (ImageView) getActivity().findViewById(R.id.space23);
		spaces[0][1][2][1] = (ImageView) getActivity().findViewById(R.id.space24);
		spaces[0][1][2][2] = (ImageView) getActivity().findViewById(R.id.space25);
		
		spaces[0][2][0][0] = (ImageView) getActivity().findViewById(R.id.space06);
		spaces[0][2][0][1] = (ImageView) getActivity().findViewById(R.id.space07);
		spaces[0][2][0][2] = (ImageView) getActivity().findViewById(R.id.space08);
		spaces[0][2][1][0] = (ImageView) getActivity().findViewById(R.id.space16);
		spaces[0][2][1][1] = (ImageView) getActivity().findViewById(R.id.space17);
		spaces[0][2][1][2] = (ImageView) getActivity().findViewById(R.id.space18);
		spaces[0][2][2][0] = (ImageView) getActivity().findViewById(R.id.space26);
		spaces[0][2][2][1] = (ImageView) getActivity().findViewById(R.id.space27);
		spaces[0][2][2][2] = (ImageView) getActivity().findViewById(R.id.space28);
		
		spaces[1][0][0][0] = (ImageView) getActivity().findViewById(R.id.space30);
		spaces[1][0][0][1] = (ImageView) getActivity().findViewById(R.id.space31);
		spaces[1][0][0][2] = (ImageView) getActivity().findViewById(R.id.space32);
		spaces[1][0][1][0] = (ImageView) getActivity().findViewById(R.id.space40);
		spaces[1][0][1][1] = (ImageView) getActivity().findViewById(R.id.space41);
		spaces[1][0][1][2] = (ImageView) getActivity().findViewById(R.id.space42);
		spaces[1][0][2][0] = (ImageView) getActivity().findViewById(R.id.space50);
		spaces[1][0][2][1] = (ImageView) getActivity().findViewById(R.id.space51);
		spaces[1][0][2][2] = (ImageView) getActivity().findViewById(R.id.space52);
		
		spaces[1][1][0][0] = (ImageView) getActivity().findViewById(R.id.space33);
		spaces[1][1][0][1] = (ImageView) getActivity().findViewById(R.id.space34);
		spaces[1][1][0][2] = (ImageView) getActivity().findViewById(R.id.space35);
		spaces[1][1][1][0] = (ImageView) getActivity().findViewById(R.id.space43);
		spaces[1][1][1][1] = (ImageView) getActivity().findViewById(R.id.space44);
		spaces[1][1][1][2] = (ImageView) getActivity().findViewById(R.id.space45);
		spaces[1][1][2][0] = (ImageView) getActivity().findViewById(R.id.space53);
		spaces[1][1][2][1] = (ImageView) getActivity().findViewById(R.id.space54);
		spaces[1][1][2][2] = (ImageView) getActivity().findViewById(R.id.space55);
		
		spaces[1][2][0][0] = (ImageView) getActivity().findViewById(R.id.space36);
		spaces[1][2][0][1] = (ImageView) getActivity().findViewById(R.id.space37);
		spaces[1][2][0][2] = (ImageView) getActivity().findViewById(R.id.space38);
		spaces[1][2][1][0] = (ImageView) getActivity().findViewById(R.id.space46);
		spaces[1][2][1][1] = (ImageView) getActivity().findViewById(R.id.space47);
		spaces[1][2][1][2] = (ImageView) getActivity().findViewById(R.id.space48);
		spaces[1][2][2][0] = (ImageView) getActivity().findViewById(R.id.space56);
		spaces[1][2][2][1] = (ImageView) getActivity().findViewById(R.id.space57);
		spaces[1][2][2][2] = (ImageView) getActivity().findViewById(R.id.space58);
		
		spaces[2][0][0][0] = (ImageView) getActivity().findViewById(R.id.space60);
		spaces[2][0][0][1] = (ImageView) getActivity().findViewById(R.id.space61);
		spaces[2][0][0][2] = (ImageView) getActivity().findViewById(R.id.space62);
		spaces[2][0][1][0] = (ImageView) getActivity().findViewById(R.id.space70);
		spaces[2][0][1][1] = (ImageView) getActivity().findViewById(R.id.space71);
		spaces[2][0][1][2] = (ImageView) getActivity().findViewById(R.id.space72);
		spaces[2][0][2][0] = (ImageView) getActivity().findViewById(R.id.space80);
		spaces[2][0][2][1] = (ImageView) getActivity().findViewById(R.id.space81);
		spaces[2][0][2][2] = (ImageView) getActivity().findViewById(R.id.space82);
		
		spaces[2][1][0][0] = (ImageView) getActivity().findViewById(R.id.space63);
		spaces[2][1][0][1] = (ImageView) getActivity().findViewById(R.id.space64);
		spaces[2][1][0][2] = (ImageView) getActivity().findViewById(R.id.space65);
		spaces[2][1][1][0] = (ImageView) getActivity().findViewById(R.id.space73);
		spaces[2][1][1][1] = (ImageView) getActivity().findViewById(R.id.space74);
		spaces[2][1][1][2] = (ImageView) getActivity().findViewById(R.id.space75);
		spaces[2][1][2][0] = (ImageView) getActivity().findViewById(R.id.space83);
		spaces[2][1][2][1] = (ImageView) getActivity().findViewById(R.id.space84);
		spaces[2][1][2][2] = (ImageView) getActivity().findViewById(R.id.space85);
		
		spaces[2][2][0][0] = (ImageView) getActivity().findViewById(R.id.space66);
		spaces[2][2][0][1] = (ImageView) getActivity().findViewById(R.id.space67);
		spaces[2][2][0][2] = (ImageView) getActivity().findViewById(R.id.space68);
		spaces[2][2][1][0] = (ImageView) getActivity().findViewById(R.id.space76);
		spaces[2][2][1][1] = (ImageView) getActivity().findViewById(R.id.space77);
		spaces[2][2][1][2] = (ImageView) getActivity().findViewById(R.id.space78);
		spaces[2][2][2][0] = (ImageView) getActivity().findViewById(R.id.space86);
		spaces[2][2][2][1] = (ImageView) getActivity().findViewById(R.id.space87);
		spaces[2][2][2][2] = (ImageView) getActivity().findViewById(R.id.space88);
		
		//Log.i("GameBoardFragment", "setting listeners");
		//set on click listeners
		spaces[0][0][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,0,0);
			}
		});
		spaces[0][0][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,0,1);
			}
		});
		spaces[0][0][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,0,2);
			}
		});
		spaces[0][0][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,1,0);
			}
		});
		spaces[0][0][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,1,1);
			}
		});
		spaces[0][0][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,1,2);
			}
		});
		spaces[0][0][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,2,0);
			}
		});
		spaces[0][0][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,2,1);
			}
		});
		spaces[0][0][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,2,2);
			}
		});
		spaces[0][1][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,0,0);
			}
		});
		spaces[0][1][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,0,1);
			}
		});
		spaces[0][1][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,0,2);
			}
		});
		spaces[0][1][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,1,0);
			}
		});
		spaces[0][1][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,1,1);
			}
		});
		spaces[0][1][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,1,2);
			}
		});
		spaces[0][1][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,2,0);
			}
		});
		spaces[0][1][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,2,1);
			}
		});
		spaces[0][1][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,2,2);
			}
		});
		spaces[0][2][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,0,0);
			}
		});
		spaces[0][2][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,0,1);
			}
		});
		spaces[0][2][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,0,2);
			}
		});
		spaces[0][2][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,1,0);
			}
		});
		spaces[0][2][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,1,1);
			}
		});
		spaces[0][2][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,1,2);
			}
		});
		spaces[0][2][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,2,0);
			}
		});
		spaces[0][2][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,2,1);
			}
		});
		spaces[0][2][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,2,2);
			}
		});
		spaces[1][0][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,0,0);
			}
		});
		spaces[1][0][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,0,1);
			}
		});
		spaces[1][0][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,0,2);
			}
		});
		spaces[1][0][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,1,0);
			}
		});
		spaces[1][0][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,1,1);
			}
		});
		spaces[1][0][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,1,2);
			}
		});
		spaces[1][0][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,2,0);
			}
		});
		spaces[1][0][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,2,1);
			}
		});
		spaces[1][0][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,2,2);
			}
		});
		spaces[1][1][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,0,0);
			}
		});
		spaces[1][1][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,0,1);
			}
		});
		spaces[1][1][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,0,2);
			}
		});
		spaces[1][1][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,1,0);
			}
		});
		spaces[1][1][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,1,1);
			}
		});
		spaces[1][1][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,1,2);
			}
		});
		spaces[1][1][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,2,0);
			}
		});
		spaces[1][1][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,2,1);
			}
		});
		spaces[1][1][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,2,2);
			}
		});
		spaces[1][2][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,0,0);
			}
		});
		spaces[1][2][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,0,1);
			}
		});
		spaces[1][2][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,0,2);
			}
		});
		spaces[1][2][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,1,0);
			}
		});
		spaces[1][2][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,1,1);
			}
		});
		spaces[1][2][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,1,2);
			}
		});
		spaces[1][2][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,2,0);
			}
		});
		spaces[1][2][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,2,1);
			}
		});
		spaces[1][2][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,2,2);
			}
		});
		spaces[2][0][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,0,0);
			}
		});
		spaces[2][0][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,0,1);
			}
		});
		spaces[2][0][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,0,2);
			}
		});
		spaces[2][0][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,1,0);
			}
		});
		spaces[2][0][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,1,1);
			}
		});
		spaces[2][0][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,1,2);
			}
		});
		spaces[2][0][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,2,0);
			}
		});
		spaces[2][0][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,2,1);
			}
		});
		spaces[2][0][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,2,2);
			}
		});
		spaces[2][1][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,0,0);
			}
		});
		spaces[2][1][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,0,1);
			}
		});
		spaces[2][1][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,0,2);
			}
		});
		spaces[2][1][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,1,0);
			}
		});
		spaces[2][1][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,1,1);
			}
		});
		spaces[2][1][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,1,2);
			}
		});
		spaces[2][1][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,2,0);
			}
		});
		spaces[2][1][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,2,1);
			}
		});
		spaces[2][1][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,2,2);
			}
		});
		spaces[2][2][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,0,0);
			}
		});
		spaces[2][2][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,0,1);
			}
		});
		spaces[2][2][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,0,2);
			}
		});
		spaces[2][2][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,1,0);
			}
		});
		spaces[2][2][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,1,1);
			}
		});
		spaces[2][2][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,1,2);
			}
		});
		spaces[2][2][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,2,0);
			}
		});
		spaces[2][2][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,2,1);
			}
		});
		spaces[2][2][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,2,2);
			}
		});
	}
}
