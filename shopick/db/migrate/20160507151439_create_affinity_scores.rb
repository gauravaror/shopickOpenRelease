class CreateAffinityScores < ActiveRecord::Migration
  def change
    create_table :affinity_scores do |t|
      t.decimal :affinity_scores
      t.belongs_to :user, index:true
      t.belongs_to :brand, index:true
      t.belongs_to :category, index:true
      t.timestamps null: false
    end
  end
end
