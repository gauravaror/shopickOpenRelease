class AffinityScore < ActiveRecord::Base
	belongs_to :brand
    belongs_to :category
    belongs_to :user

    def self.process_post_like(user, post)
        if post.store
            if user.affinity_scores.where(brand_id: post.store.brand_id).size > 0
                aff = user.affinity_scores.where(brand_id: post.store.brand_id).first
                aff.update(affinity_scores: aff.affinity_scores + 10)
            else
                user.affinity_scores.create(affinity_scores: 10, brand_id: post.store.brand_id)
            end
        end
    end

    def self.process_product_like(user, product)
        if product.brand
            if user.affinity_scores.where(brand_id: product.brand_id).size > 0
                aff = user.affinity_scores.where(brand_id: product.brand_id).first
                aff.update(affinity_scores: aff.affinity_scores + 10)
            else
                user.affinity_scores.create(affinity_scores: 10, brand_id: product.brand_id)
            end
        end
    end

    def self.process_banner_like(user, banner)
        if banner.brand
            if user.affinity_scores.where(brand_id: banner.brand_id).size > 0
                aff = user.affinity_scores.where(brand_id: banner.brand_id).first
                aff.update(affinity_scores: aff.affinity_scores + 10)
            else
                user.affinity_scores.create(affinity_scores: 10, brand_id: banner.brand_id)
            end
        end

    end

    def self.process_tips_like(user, tip)
        if tip.brand
            if user.affinity_scores.where(brand_id: tip.brand_id).size > 0
                aff = user.affinity_scores.where(brand_id: tip.brand_id).first
                aff.update(affinity_scores: aff.affinity_scores + 10)
            else
                user.affinity_scores.create(affinity_scores: 10, brand_id: tip.brand_id)
            end
        end

    end

    def self.process_brand_like(user, brand)
        if brand
            if user.affinity_scores.where(brand_id: brand.id).size > 0
                aff = user.affinity_scores.where(brand_id: brand.id).first
                aff.update(affinity_scores: aff.affinity_scores + 30)
            else
                user.affinity_scores.create(affinity_scores: 30, brand_id: brand.id)
            end
        end
    end

    def self.process_post_collection_like(user, post_collection)
        if post_collection.brand
            if user.affinity_scores.where(brand_id: post_collection.brand_id).size > 0
                aff = user.affinity_scores.where(brand_id: post_collection.brand_id).first
                aff.update(affinity_scores: aff.affinity_scores + 10)
            else
                user.affinity_scores.create(affinity_scores: 10, brand_id: post_collection.brand_id)
            end
        end
    end

    def self.process_brand_update_like(user, brand_update)
        if brand_update.brand
            if user.affinity_scores.where(brand_id: brand_update.brand_id).size > 0
                aff = user.affinity_scores.where(brand_id: brand_update.brand_id).first
                aff.update(affinity_scores: aff.affinity_scores + 10)
            else
                user.affinity_scores.create(affinity_scores: 10, brand_id: brand_update.brand_id)
            end
        end
    end




    def self.process_post_unlike(user, post)
        if post.store
            if user.affinity_scores.where(brand_id: post.store.brand_id).size > 0
                aff = user.affinity_scores.where(brand_id: post.store.brand_id).first
                aff.update(affinity_scores: aff.affinity_scores - 9)
            end
        end
    end

    def self.process_product_unlike(user, product)
        if product.brand
            if user.affinity_scores.where(brand_id: product.brand_id).size > 0
                aff = user.affinity_scores.where(brand_id: product.brand_id).first
                aff.update(affinity_scores: aff.affinity_scores - 9)
            end
        end
    end

    def self.process_banner_unlike(user, banner)
        if banner.brand
            if user.affinity_scores.where(brand_id: banner.brand_id).size > 0
                aff = user.affinity_scores.where(brand_id: banner.brand_id).first
                aff.update(affinity_scores: aff.affinity_scores - 9)
            end
        end

    end

    def self.process_tips_unlike(user, tip)
        if tip.brand
            if user.affinity_scores.where(brand_id: tip.brand_id).size > 0
                aff = user.affinity_scores.where(brand_id: tip.brand_id).first
                aff.update(affinity_scores: aff.affinity_scores - 9)
            end
        end

    end

    def self.process_brand_unlike(user, brand)
        if brand
            if user.affinity_scores.where(brand_id: brand.id).size > 0
                aff = user.affinity_scores.where(brand_id: brand.id).first
                aff.update(affinity_scores: aff.affinity_scores - 28)
            end
        end
    end

    def self.process_post_collection_unlike(user, post_collection)
        if post_collection.brand
            if user.affinity_scores.where(brand_id: post_collection.brand_id).size > 0
                aff = user.affinity_scores.where(brand_id: post_collection.brand_id).first
                aff.update(affinity_scores: aff.affinity_scores - 9)
            end
        end
    end

    def self.process_brand_update_unlike(user, brand_update)
        if brand_update.brand
            if user.affinity_scores.where(brand_id: brand_update.brand_id).size > 0
                aff = user.affinity_scores.where(brand_id: brand_update.brand_id).first
                aff.update(affinity_scores: aff.affinity_scores - 9)
            end
        end
    end

    def process_user(user)
    end

    def recalculate_affinity_score()
    end


end
